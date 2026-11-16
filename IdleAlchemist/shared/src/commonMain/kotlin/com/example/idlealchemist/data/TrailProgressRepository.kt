package com.example.idlealchemist.data

import com.example.idlealchemist.feature.trail.AvailableLevelMaps
import com.example.idlealchemist.feature.trail.CrucibleTrailStages
import com.example.idlealchemist.model.LevelMap
import com.example.idlealchemist.model.LevelProgress
import com.example.idlealchemist.model.ProfileProgress
import com.example.idlealchemist.model.StageStatus
import com.example.idlealchemist.model.TrailStage
import com.example.idlealchemist.model.achievementsGame
import com.example.idlealchemist.model.afterBuyApprentice
import com.example.idlealchemist.model.afterBuyExtraClick
import com.example.idlealchemist.model.afterBuyExtraHand
import com.example.idlealchemist.model.afterBuyPotion
import com.example.idlealchemist.model.afterDeleteAchievement
import com.example.idlealchemist.model.afterTap
import com.example.idlealchemist.model.afterTick
import com.example.idlealchemist.model.stageStatuses
import com.example.idlealchemist.model.withAchievements
import com.example.idlealchemist.model.withUnlocks
import kotlin.time.TimeSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Keeps every alchemist's progress through level maps for the session.
 *
 * Progress is stored per profile and per stage, so each stage is a
 * separate clicker game that resumes where the player left it.
 * Achievements are earned per profile from every stage combined. Actions
 * on a locked or unknown stage are ignored.
 *
 * @property availableMaps All loaded level maps available in the application.
 * @param nowMillis Clock used for combo windows; replaceable in tests.
 */
class TrailProgressRepository(
    val availableMaps: List<LevelMap> = AvailableLevelMaps,
    initialMapId: Int = 1,
    private val nowMillis: () -> Long = monotonicMillis(),
) {
    /** Secondary constructor allowing stage list initialization for tests. */
    constructor(
        stages: List<TrailStage>,
        nowMillis: () -> Long = monotonicMillis(),
    ) : this(
        availableMaps = listOf(LevelMap(id = 1, worldName = "The Alchemist's Tome: Pinewoods", levels = stages)),
        initialMapId = 1,
        nowMillis = nowMillis,
    )

    private val mutableProgress =
        MutableStateFlow<Map<String, ProfileProgress>>(emptyMap())

    /** Saved trail progress keyed by profile ID. */
    val progress: StateFlow<Map<String, ProfileProgress>> =
        mutableProgress.asStateFlow()

    private val _currentMapId = MutableStateFlow(initialMapId)

    /** Currently active internal level map ID. */
    val currentMapId: StateFlow<Int> = _currentMapId.asStateFlow()

    /** Currently active level map. */
    val currentLevelMap: LevelMap
        get() = availableMaps.firstOrNull { it.id == _currentMapId.value } ?: availableMaps.first()

    /** Stages of the currently active level map. */
    val stages: List<TrailStage>
        get() = currentLevelMap.levels

    /** Switches the active level map by its internal level map ID. */
    fun selectMap(mapId: Int) {
        if (availableMaps.any { it.id == mapId }) {
            _currentMapId.value = mapId
        }
    }

    /**
     * Applies a tap to the stage's game and checks its goal.
     *
     * @param profileId Alchemist playing the stage.
     * @param stageNumber Stage being played.
     */
    fun tap(profileId: String, stageNumber: Int) {
        val stage = stages.firstOrNull { it.number == stageNumber } ?: return
        val now = nowMillis()
        updateLevel(profileId, stageNumber) { level -> level.afterTap(stage.goal, now) }
    }

    /** Buys an extra click inside one stage's game. */
    fun buyExtraClick(profileId: String, stageNumber: Int) =
        updateLevel(profileId, stageNumber) { level ->
            level.copy(game = level.game.afterBuyExtraClick())
        }

    /** Hires an apprentice inside one stage's game. */
    fun buyApprentice(profileId: String, stageNumber: Int) =
        updateLevel(profileId, stageNumber) { level ->
            level.copy(game = level.game.afterBuyApprentice())
        }

    fun buyExtraHand(profileId: String, stageNumber: Int) =
        updateLevel(profileId, stageNumber) { level ->
            level.copy(game = level.game.afterBuyExtraHand())
        }

    /**
     * Activates a potion in the stage the shop boosts: the last stage
     * played, or the first stage before any has been played.
     *
     * @param profileId Alchemist who bought the potion.
     * @param potionId Potion handed out after the mock payment.
     */
    fun buyPotion(profileId: String, potionId: String) =
        updateLevel(profileId, shopStage(profileId)) { level ->
            level.copy(game = level.game.afterBuyPotion(potionId))
        }

    /**
     * Advances one second inside the stage currently being played; the
     * achievement message counts down at the same time.
     */
    fun tick(profileId: String, stageNumber: Int) {
        updateLevel(profileId, stageNumber) { level ->
            level.copy(game = level.game.afterTick())
        }
        updateProfile(profileId) { profile ->
            val notice = profile.achievements.notice
            profile.copy(
                achievements = profile.achievements.copy(
                    notice = notice?.takeIf { it.secondsLeft > 1 }
                        ?.copy(secondsLeft = notice.secondsLeft - 1),
                ),
            )
        }
    }

    /**
     * Removes an owned achievement so it can be earned again from zero.
     *
     * @param profileId Alchemist who owns the achievement.
     * @param achievementId Achievement to remove.
     */
    fun deleteAchievement(profileId: String, achievementId: String) =
        updateProfile(profileId) { profile ->
            profile.withAchievements(profile.achievementsGame().afterDeleteAchievement(achievementId))
        }

    /**
     * Stage that shop potions boost for an alchemist.
     *
     * @param profileId Alchemist using the shop.
     */
    fun shopStage(profileId: String): Int =
        mutableProgress.value[profileId]?.lastStage ?: stages.first().number

    /**
     * Forgets everything saved for a removed alchemist.
     *
     * @param profileId Identifier removed from the profile repository.
     */
    fun removeProfile(profileId: String) =
        mutableProgress.update { all -> all - profileId }

    private fun updateLevel(
        profileId: String,
        stageNumber: Int,
        change: (LevelProgress) -> LevelProgress,
    ) = updateProfile(profileId) { profile ->
        val status = stageStatuses(stages, profile.levels)[stageNumber]

        if (status == null || status == StageStatus.LOCKED) {
            profile
        } else {
            val level = profile.levels[stageNumber] ?: LevelProgress()
            val changed = profile.copy(
                levels = profile.levels + (stageNumber to change(level)),
                lastStage = stageNumber,
            )
            changed.withAchievements(changed.achievementsGame().withUnlocks())
        }
    }

    private fun updateProfile(
        profileId: String,
        change: (ProfileProgress) -> ProfileProgress,
    ) = mutableProgress.update { all ->
        all + (profileId to change(all[profileId] ?: ProfileProgress()))
    }
}

/** Milliseconds since this clock was created, unaffected by changes to the device time. */
private fun monotonicMillis(): () -> Long {
    val start = TimeSource.Monotonic.markNow()
    return { start.elapsedNow().inWholeMilliseconds }
}

package com.example.idlealchemist.model

/**
 * Everything saved for one alchemist on the trail.
 *
 * @property levels Saved progress keyed by stage number.
 * @property achievements Achievements earned across every stage.
 * @property lastStage Stage most recently played, which shop potions boost.
 * Null until the first stage is played.
 */
data class ProfileProgress(
    val levels: Map<Int, LevelProgress> = emptyMap(),
    val achievements: ProfileAchievements = ProfileAchievements(),
    val lastStage: Int? = null,
)

/**
 * Achievements belong to the alchemist, not to one stage. Their progress is
 * counted from every stage's stats added together, so clicks in stage 1 and
 * stage 2 both count toward "Click 100 Times".
 *
 * @property owned IDs of the achievements earned.
 * @property baselines Counter value at the moment an achievement was deleted.
 * @property notice "Achievement unlocked" message still being shown.
 */
data class ProfileAchievements(
    val owned: Set<String> = emptySet(),
    val baselines: Map<String, Int> = emptyMap(),
    val notice: UnlockNotice? = null,
)

/** Every stage's counters added together. */
fun ProfileProgress.totalStats(): GameStats =
    levels.values.map { it.game.stats }.fold(GameStats()) { total, stats ->
        GameStats(
            manualClicks = total.manualClicks + stats.manualClicks,
            brewed = total.brewed + stats.brewed,
            apprenticesHired = total.apprenticesHired + stats.apprenticesHired,
            extraClicksBought = total.extraClicksBought + stats.extraClicksBought,
            potionsUsed = total.potionsUsed + stats.potionsUsed,
        )
    }

/**
 * The alchemist's achievements as a game state, so the achievement rules in
 * AchievementStatus.kt and the achievements screen work unchanged.
 */
fun ProfileProgress.achievementsGame(): GameState = GameState(
    stats = totalStats(),
    ownedAchievements = achievements.owned,
    baselines = achievements.baselines,
    unlockNotice = achievements.notice,
)

/**
 * Stores achievement changes made on [game] (from [achievementsGame]).
 *
 * @param game Achievement state after awarding, deleting, or ticking.
 */
fun ProfileProgress.withAchievements(game: GameState): ProfileProgress = copy(
    achievements = ProfileAchievements(
        owned = game.ownedAchievements,
        baselines = game.baselines,
        notice = game.unlockNotice,
    ),
)

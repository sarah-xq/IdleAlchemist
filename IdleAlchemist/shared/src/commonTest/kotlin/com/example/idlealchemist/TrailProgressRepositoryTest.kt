package com.example.idlealchemist

import com.example.idlealchemist.data.TrailProgressRepository
import com.example.idlealchemist.model.ProfileAchievements
import com.example.idlealchemist.model.StageGoal
import com.example.idlealchemist.model.StageStatus
import com.example.idlealchemist.model.TrailStage
import com.example.idlealchemist.model.stageStatuses
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TrailProgressRepositoryTest {

    private val stages = listOf(
        stage(1, StageGoal.Taps(target = 3)),
        stage(2, StageGoal.Taps(target = 5)),
        stage(3, StageGoal.Combo(target = 3, windowSeconds = 2)),
    )

    private var now = 0L
    private val repository = TrailProgressRepository(stages) { now }

    private fun levels(profileId: String = "a") = repository.progress.value[profileId]?.levels.orEmpty()

    private fun statuses(profileId: String = "a") = stageStatuses(stages, levels(profileId))

    private fun tap(stageNumber: Int, times: Int, profileId: String = "a") =
        repeat(times) { repository.tap(profileId, stageNumber) }

    @Test
    fun onlyTheFirstStageStartsUnlocked() {
        assertEquals(
            mapOf(1 to StageStatus.UNLOCKED, 2 to StageStatus.LOCKED, 3 to StageStatus.LOCKED),
            statuses(),
        )
    }

    @Test
    fun lockedStageIgnoresTaps() {
        tap(stageNumber = 2, times = 4)

        assertNull(levels()[2])
    }

    @Test
    fun reachingTheGoalCompletesTheStageAndUnlocksTheNext() {
        tap(stageNumber = 1, times = 2)
        assertEquals(StageStatus.LOCKED, statuses()[2])

        tap(stageNumber = 1, times = 1)

        assertEquals(StageStatus.COMPLETED, statuses()[1])
        assertEquals(StageStatus.UNLOCKED, statuses()[2])
        assertEquals(StageStatus.LOCKED, statuses()[3])
    }

    @Test
    fun pointsStartAtZeroInEachNewStage() {
        tap(stageNumber = 1, times = 3)
        assertEquals(3, levels()[1]?.game?.points)

        tap(stageNumber = 2, times = 1)

        assertEquals(1, levels()[2]?.game?.points)
        assertEquals(1, levels()[2]?.game?.stats?.manualClicks)
    }

    @Test
    fun returningToACompletedStageKeepsItsSavedGame() {
        tap(stageNumber = 1, times = 12)
        repository.buyExtraClick("a", 1)
        tap(stageNumber = 2, times = 2)

        val saved = levels()[1]
        assertEquals(2, saved?.game?.points)
        assertEquals(2, saved?.game?.clickPower)

        tap(stageNumber = 1, times = 1)

        assertEquals(4, levels()[1]?.game?.points)
        assertTrue(levels()[1]?.completed == true)
        assertEquals(2, levels()[2]?.game?.points)
    }

    @Test
    fun tickOnlyChangesTheStageBeingPlayed() {
        tap(stageNumber = 1, times = 3)
        tap(stageNumber = 2, times = 5)

        repository.tick("a", 2)

        assertEquals(3, levels()[1]?.game?.points)
        assertEquals(5, levels()[2]?.game?.points)
    }

    @Test
    fun comboNeedsEnoughTapsInsideOneWindow() {
        tap(stageNumber = 1, times = 3)
        tap(stageNumber = 2, times = 5)

        // Two quick taps, then the window moves past them before the third.
        tap(stageNumber = 3, times = 2)
        now = 2_500L
        tap(stageNumber = 3, times = 1)
        now = 3_000L
        tap(stageNumber = 3, times = 1)

        assertEquals(2, levels()[3]?.bestCombo)
        assertFalse(levels()[3]?.completed == true)

        // 2.5s, 3s and 3.5s all fit inside one 2-second window.
        now = 3_500L
        tap(stageNumber = 3, times = 1)

        assertEquals(3, levels()[3]?.bestCombo)
        assertTrue(levels()[3]?.completed == true)
    }

    @Test
    fun eachAlchemistHasSeparateProgress() {
        tap(stageNumber = 1, times = 3, profileId = "a")

        assertEquals(StageStatus.UNLOCKED, statuses("a")[2])
        assertEquals(StageStatus.LOCKED, statuses("b")[2])

        repository.removeProfile("a")

        assertTrue(levels("a").isEmpty())
    }

    @Test
    fun achievementsCountEveryStageTogether() {
        tap(stageNumber = 1, times = 60)
        tap(stageNumber = 2, times = 39)
        assertFalse("click_100" in achievements().owned)

        tap(stageNumber = 2, times = 1)

        assertTrue("click_100" in achievements().owned)
        assertEquals("Click 100 Times", achievements().notice?.title)
    }

    @Test
    fun deletedAchievementIsEarnedAgainFromZero() {
        tap(stageNumber = 1, times = 100)
        repository.deleteAchievement("a", "click_100")
        assertFalse("click_100" in achievements().owned)

        tap(stageNumber = 1, times = 99)
        assertFalse("click_100" in achievements().owned)

        tap(stageNumber = 1, times = 1)
        assertTrue("click_100" in achievements().owned)
    }

    @Test
    fun shopPotionBoostsTheLastStagePlayed() {
        assertEquals(1, repository.shopStage("a"))

        tap(stageNumber = 1, times = 3)
        tap(stageNumber = 2, times = 1)
        repository.buyPotion("a", "double_tap")

        assertTrue(levels()[1]?.game?.activePotions.orEmpty().isEmpty())
        assertEquals("double_tap", levels()[2]?.game?.activePotions?.single()?.potionId)
        assertTrue("potion_taster" in achievements().owned)

        tap(stageNumber = 2, times = 1)
        assertEquals(3, levels()[2]?.game?.points)
    }

    private fun achievements(profileId: String = "a") =
        repository.progress.value[profileId]?.achievements ?: ProfileAchievements()

    private fun stage(number: Int, goal: StageGoal) = TrailStage(
        number = number,
        title = "Stage $number",
        element = "Fire",
        objective = "",
        reward = "",
        goal = goal,
    )
}

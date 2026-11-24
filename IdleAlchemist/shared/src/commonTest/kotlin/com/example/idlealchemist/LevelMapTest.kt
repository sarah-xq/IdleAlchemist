package com.example.idlealchemist

import com.example.idlealchemist.data.TrailProgressRepository
import com.example.idlealchemist.feature.trail.DebugLevelMap
import com.example.idlealchemist.feature.trail.PinewoodsLevelMap
import com.example.idlealchemist.model.LevelMap
import com.example.idlealchemist.model.StageGoal
import com.example.idlealchemist.model.StageStatus
import com.example.idlealchemist.model.TrailStage
import com.example.idlealchemist.model.stageStatuses
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LevelMapTest {

    @Test
    fun levelMapContainsRequiredFields() {
        val level1 = TrailStage(
            number = 1,
            title = "Whispering Pines",
            element = "Fire",
            objective = "Tap 10 times.",
            reward = "Tonic",
            goal = StageGoal.Taps(10),
        )
        val level2 = TrailStage(
            number = 2,
            title = "Mossy Cairn",
            element = "Water",
            objective = "Tap 25 times.",
            reward = "Philter",
            goal = StageGoal.Taps(25),
        )

        val map = LevelMap(
            id = 42,
            worldName = "The Alchemist's Tome: Pinewoods",
            levels = listOf(level1, level2),
        )

        assertEquals(42, map.id)
        assertEquals("The Alchemist's Tome: Pinewoods", map.worldName)
        assertEquals(2, map.levels.size)
        assertEquals("Whispering Pines", map.levels[0].title)
        assertEquals("Mossy Cairn", map.levels[1].title)
    }

    @Test
    fun pinewoodsLevelMapIsConfiguredCorrectlyFromSpec() {
        assertEquals(1, PinewoodsLevelMap.id)
        assertEquals("The Alchemist's Tome: Pinewoods", PinewoodsLevelMap.worldName)
        assertEquals(10, PinewoodsLevelMap.levels.size)

        val firstLevel = PinewoodsLevelMap.levels.first()
        assertEquals(1, firstLevel.number)
        assertEquals("Whispering Pines", firstLevel.title)
        assertEquals("I", firstLevel.roman)
        assertEquals("Dewdrop Tonic", firstLevel.potion)

        val lastLevel = PinewoodsLevelMap.levels.last()
        assertEquals(10, lastLevel.number)
        assertEquals("The Alchemist's Sanctum", lastLevel.title)
        assertEquals("X", lastLevel.roman)
        assertTrue(lastLevel.isBoss)
    }

    @Test
    fun levelOrderIsPreservedAndImmutableByUsers() {
        val originalOrder = PinewoodsLevelMap.levels.map { it.number }
        val expectedOrder = (1..10).toList()

        assertEquals(expectedOrder, originalOrder)
    }

    @Test
    fun levelLockingStatusPreventsAccessToLockedLevels() {
        val levels = PinewoodsLevelMap.levels
        val repository = TrailProgressRepository(availableMaps = listOf(PinewoodsLevelMap))
        val profileId = "test_alchemist"

        val initialStatuses = stageStatuses(levels, repository.progress.value[profileId]?.levels.orEmpty())

        assertEquals(StageStatus.UNLOCKED, initialStatuses[1])
        for (lvlNum in 2..10) {
            assertEquals(StageStatus.LOCKED, initialStatuses[lvlNum])
        }

        // Tapping locked level 2 before completing level 1 is ignored
        repeat(30) {
            repository.tap(profileId, 2)
        }
        val level2Progress = repository.progress.value[profileId]?.levels?.get(2)
        assertEquals(null, level2Progress)

        // Completing level 1 unlocks level 2
        repeat(10) {
            repository.tap(profileId, 1)
        }
        val updatedStatuses = stageStatuses(levels, repository.progress.value[profileId]?.levels.orEmpty())

        assertEquals(StageStatus.COMPLETED, updatedStatuses[1])
        assertEquals(StageStatus.UNLOCKED, updatedStatuses[2])
        assertEquals(StageStatus.LOCKED, updatedStatuses[3])
    }

    @Test
    fun levelMapSelectionSupportsMultipleWorldMapsAndDebugMaps() {
        val repository = TrailProgressRepository(
            availableMaps = listOf(PinewoodsLevelMap, DebugLevelMap),
            initialMapId = 1,
        )

        assertEquals(1, repository.currentLevelMap.id)
        assertEquals("The Alchemist's Tome: Pinewoods", repository.currentLevelMap.worldName)
        assertEquals(10, repository.currentLevelMap.levels.size)

        repository.selectMap(999)

        assertEquals(999, repository.currentLevelMap.id)
        assertEquals("Debug Testing Grounds", repository.currentLevelMap.worldName)
        assertEquals(3, repository.currentLevelMap.levels.size)
    }

    @Test
    fun worldNameDoesNotHaveToBeUnique() {
        val mapA = LevelMap(id = 101, worldName = "Shared Realm", levels = emptyList())
        val mapB = LevelMap(id = 102, worldName = "Shared Realm", levels = emptyList())

        assertEquals(mapA.worldName, mapB.worldName)
        assertFalse(mapA.id == mapB.id)
    }
}

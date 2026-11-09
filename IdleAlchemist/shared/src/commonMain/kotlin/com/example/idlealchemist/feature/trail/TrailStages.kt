package com.example.idlealchemist.feature.trail

import com.example.idlealchemist.model.StageGoal
import com.example.idlealchemist.model.TrailStage

/**
 * Supplies the ordered stage definitions for the first trail.
 *
 * The definitions are shared by the stage map, challenge dialog, and
 * the playable stage screen. Stages unlock in list order.
 */
val CrucibleTrailStages: List<TrailStage> = listOf(
    TrailStage(
        number = 1,
        title = "First Spark",
        element = "Fire",
        objective = "Tap the crucible 10 times to kindle your first essence.",
        reward = "Ember Essence recipe",
        goal = StageGoal.Taps(target = 10),
    ),
    TrailStage(
        number = 2,
        title = "Quiet Waters",
        element = "Water",
        objective = "Tap the crucible 25 times to distill a clear essence.",
        reward = "Dewdrop Tincture recipe",
        goal = StageGoal.Taps(target = 25),
    ),
    TrailStage(
        number = 3,
        title = "Root and Stone",
        element = "Earth",
        objective = "Tap the crucible 50 times to refine mineral fragments.",
        reward = "Verdant Salt recipe",
        goal = StageGoal.Taps(target = 50),
    ),
    TrailStage(
        number = 4,
        title = "Gathering Winds",
        element = "Air",
        objective = "Complete a combo of 15 taps within 10 seconds.",
        reward = "Zephyr Catalyst recipe",
        goal = StageGoal.Combo(target = 15, windowSeconds = 10),
    ),
    TrailStage(
        number = 5,
        title = "The Violet Flame",
        element = "Aether",
        objective = "Complete a combo of 30 taps within 15 seconds.",
        reward = "Astral Ink recipe",
        goal = StageGoal.Combo(target = 30, windowSeconds = 15),
    ),
    TrailStage(
        number = 6,
        title = "The Great Transmutation",
        element = "All elements",
        objective = "Tap the crucible 150 times to combine the five essences.",
        reward = "Apprentice's Seal artifact",
        goal = StageGoal.Taps(target = 150),
    ),
)

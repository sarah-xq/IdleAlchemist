package com.example.idlealchemist.model

/**
 * Describes one stage displayed on the Crucible Trail.
 *
 * Each stage is played as its own clicker game. Completing its [goal]
 * unlocks the next stage; see [LevelProgress] for what is saved per stage.
 *
 * @property number Unique positive stage number used as its list identity.
 * @property title Display name of the stage.
 * @property element Element associated with the challenge.
 * @property objective Instructions describing the challenge.
 * @property reward Reward associated with future stage completion.
 * @property goal Measurable requirement that completes the stage.
 */
data class TrailStage(
    val number: Int,
    val title: String,
    val element: String,
    val objective: String,
    val reward: String,
    val goal: StageGoal,
)

/**
 * The requirement a stage checks to decide whether it is complete.
 */
sealed interface StageGoal {

    /** Amount the player must reach. */
    val target: Int

    /**
     * Tap the crucible [target] times in total during the stage.
     *
     * @property target Required number of manual taps.
     */
    data class Taps(
        override val target: Int,
    ) : StageGoal

    /**
     * Land [target] taps inside any window of [windowSeconds] seconds.
     *
     * @property target Required number of taps inside one window.
     * @property windowSeconds Length of the combo window in seconds.
     */
    data class Combo(
        override val target: Int,
        val windowSeconds: Int,
    ) : StageGoal
}

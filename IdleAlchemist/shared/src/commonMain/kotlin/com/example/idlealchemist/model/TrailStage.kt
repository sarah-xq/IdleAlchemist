package com.example.idlealchemist.model

/**
 * Describes one stage/level displayed on a Level Map.
 *
 * Each stage is played as its own clicker game. Completing its [goal]
 * unlocks the next stage; see [LevelProgress] for what is saved per stage.
 *
 * @property number Unique positive stage number used as its list identity.
 * @property title Display name of the stage.
 * @property element Element associated with the challenge.
 * @property objective Instructions describing the challenge.
 * @property reward Reward associated with stage completion.
 * @property goal Measurable requirement that completes the stage.
 * @property roman Roman numeral representation for RPG styling.
 * @property lore Narrative entry for the stage tome.
 * @property potion Recommended brew for the stage.
 * @property potionColorHex Color hex code for potion styling.
 * @property rewardEssence Essence points awarded upon stage completion.
 * @property isBoss Whether this stage is a boss sanctum trial.
 * @property xPercent Horizontal placement percentage on visual map (0..100).
 * @property yPx Vertical placement coordinate in pixels on visual map.
 */
data class TrailStage(
    val number: Int,
    val title: String,
    val element: String,
    val objective: String,
    val reward: String,
    val goal: StageGoal,
    val roman: String = numberToRoman(number),
    val lore: String = "",
    val potion: String = "",
    val potionColorHex: String = "#38D6D1",
    val rewardEssence: Int = 50,
    val isBoss: Boolean = false,
    val xPercent: Float = 50f,
    val yPx: Float = (2520 - (number - 1) * 250).toFloat(),
)

/** Helper to convert 1..10 to Roman numerals. */
private fun numberToRoman(number: Int): String = when (number) {
    1 -> "I"
    2 -> "II"
    3 -> "III"
    4 -> "IV"
    5 -> "V"
    6 -> "VI"
    7 -> "VII"
    8 -> "VIII"
    9 -> "IX"
    10 -> "X"
    else -> number.toString()
}

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

package com.example.idlealchemist.model

/**
 * What the player has to do to earn an achievement. Each type reads one cumulative counter from
 * [GameStats]. A sealed interface, so adding a new kind of goal is one new class.
 */
sealed interface Requirement {
    val target: Int

    /** Plural noun shown next to the progress number, e.g. "37 / 100 manual clicks". */
    val unit: String

    fun stat(stats: GameStats): Int

    data class ManualClicks(override val target: Int) : Requirement {
        override val unit: String = "manual clicks"
        override fun stat(stats: GameStats) = stats.manualClicks
    }

    data class BrewEssence(override val target: Int) : Requirement {
        override val unit: String = "essence brewed"
        override fun stat(stats: GameStats) = stats.brewed
    }

    data class HireApprentices(override val target: Int) : Requirement {
        override val unit: String = "apprentices hired"
        override fun stat(stats: GameStats) = stats.apprenticesHired
    }

    data class BuyExtraClicks(override val target: Int) : Requirement {
        override val unit: String = "Stronger Stir upgrades bought"
        override fun stat(stats: GameStats) = stats.extraClicksBought
    }

    data class UsePotions(override val target: Int) : Requirement {
        override val unit: String = "potions used"
        override fun stat(stats: GameStats) = stats.potionsUsed
    }
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val requirement: Requirement
)

/** Every achievement in the game. Add new ones here; the screens pick them up automatically. */
object Achievements {
    val all = listOf(
        Achievement("click_100", "Click 100 Times", "Click the cauldron 100 times", Requirement.ManualClicks(100)),
        Achievement("click_1000", "Click 1,000 Times", "Click the cauldron 1,000 times", Requirement.ManualClicks(1000)),
        Achievement("brew_500", "Novice Brewer", "Brew 500 essence in total", Requirement.BrewEssence(500)),
        Achievement("brew_5000", "Master Transmuter", "Brew 5,000 essence in total", Requirement.BrewEssence(5000)),
        Achievement("first_apprentice", "First Apprentice", "Hire your first apprentice", Requirement.HireApprentices(1)),
        Achievement("alchemist_guild", "Alchemist Guild", "Hire 5 apprentices", Requirement.HireApprentices(5)),
        Achievement("strong_fingers", "Strong Fingers", "Buy 5 Stronger Stir upgrades", Requirement.BuyExtraClicks(5)),
        Achievement("potion_taster", "Potion Taster", "Use your first potion", Requirement.UsePotions(1)),
    )
}

package com.example.idlealchemist.model

/**
 * Lifetime counters. They only ever go up, which is what lets an achievement be deleted and earned
 * again: deleting remembers the counter's value, and progress is counted from that point.
 */
data class GameStats(
    val manualClicks: Int = 0,
    val brewed: Int = 0,
    val apprenticesHired: Int = 0,
    val extraClicksBought: Int = 0,
    val potionsUsed: Int = 0
)

/** Short "achievement unlocked" message shown on the clicker page. */
data class UnlockNotice(val title: String, val secondsLeft: Int)

/** Everything about the current game. Immutable; the repository replaces it on every change. */
data class GameState(
    val points: Int = 0,
    val clickPower: Int = 1,
    val extraClickCost: Int = 10,
    val apprenticeCount: Int = 0,
    val autoCost: Int = 150,
    val stats: GameStats = GameStats(),
    val activePotions: List<ActivePotion> = emptyList(),
    val ownedAchievements: Set<String> = emptySet(),
    /** Counter value at the moment an achievement was deleted (absent = never deleted). */
    val baselines: Map<String, Int> = emptyMap(),
    val unlockNotice: UnlockNotice? = null,
    val extraHandCount: Int = 1,
    val extraHandCost: Int = 50
) {
    /** Points per tap after potion effects. */
    val clickGain: Int get() = clickPower * clickMultiplier() * extraHandCount

    /** Points per second from apprentices after potion effects. */
    val autoGain: Int get() = apprenticeCount * autoMultiplier()

    /** Points received from Clicks after a potion effect. */
    private fun clickMultiplier(): Int = activePotions.fold(1) { total, active ->
        val effect = Potions.byId(active.potionId)?.effect
        if (effect is PotionEffect.ClickMultiplier) total * effect.factor else total
    }
    /** Points received from apprentices after a potion effect. */
    private fun autoMultiplier(): Int = activePotions.fold(1) { total, active ->
        val effect = Potions.byId(active.potionId)?.effect
        if (effect is PotionEffect.AutoMultiplier) total * effect.factor else total
    }
}

package com.example.idlealchemist.model

/** What a potion does while it is active. */
sealed interface PotionEffect {
    /** Multiplies the points earned per tap. */
    data class ClickMultiplier(val factor: Int) : PotionEffect

    /** Multiplies the points the apprentices earn each second. */
    data class AutoMultiplier(val factor: Int) : PotionEffect
}

/** A potion sold in the shop for real-looking money ([priceCents], e.g. 99 = $0.99). */
data class Potion(
    val id: String,
    val name: String,
    val description: String,
    val priceCents: Int,
    val effect: PotionEffect,
    val durationSeconds: Int
)

/** A potion the player has drunk, with its remaining time. */
data class ActivePotion(val potionId: String, val secondsLeft: Int)

/** Everything sold in the shop. */
object Potions {
    val all = listOf(
        Potion("double_tap", "Double Tap Potion", "x2 points per tap for 30s", 99, PotionEffect.ClickMultiplier(2), 30),
        Potion("haste", "Haste Potion", "Apprentices work x2 for 30s", 199, PotionEffect.AutoMultiplier(2), 30),
        Potion("mega_tap", "Mega Tap Potion", "x5 points per tap for 15s", 299, PotionEffect.ClickMultiplier(5), 15),
    )

    fun byId(id: String): Potion? = all.firstOrNull { it.id == id }
}

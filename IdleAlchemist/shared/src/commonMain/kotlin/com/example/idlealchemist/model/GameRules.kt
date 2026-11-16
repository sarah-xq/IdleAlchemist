package com.example.idlealchemist.model

import kotlin.math.roundToInt

/*
 * The clicker's rules as pure functions: each takes a game and returns the next one.
 * GameRepository (one shared game) and TrailProgressRepository (one game per stage) both use them,
 * so a stage plays exactly like the original clicker. Achievements are awarded separately with
 * withUnlocks(), because on the trail they count every stage together. The upgrade cost formulas
 * are the ones I wrote originally.
 */

private const val GrowthFactorExtra = 1.15
private const val GrowthFactorAuto = 1.55

private const val GrowthFactorExtraHand = 1.3

/** A manual tap on the cauldron. */
fun GameState.afterTap(): GameState {
    val gain = clickGain
    return copy(
        points = points + gain,
        stats = stats.copy(
            manualClicks = stats.manualClicks + 1,
            brewed = stats.brewed + gain
        )
    )
}

fun GameState.afterBuyExtraClick(): GameState =
    if (points < extraClickCost) this else copy(
        points = points - extraClickCost,
        extraClickCost = extraClickCost + (extraClickCost * GrowthFactorExtra).roundToInt(),
        clickPower = clickPower + 1,
        stats = stats.copy(extraClicksBought = stats.extraClicksBought + 1)
    )

fun GameState.afterBuyApprentice(): GameState =
    if (points < autoCost) this else copy(
        points = points - autoCost,
        autoCost = autoCost + (autoCost * GrowthFactorAuto).roundToInt(),
        apprenticeCount = apprenticeCount + 1,
        stats = stats.copy(apprenticesHired = stats.apprenticesHired + 1)
    )

/**
 * Activates a potion. Called once the (mock) payment has gone through, so it costs no points.
 * Buying a potion that is already active adds its duration to the time left, so 15s left
 * plus a 30s potion becomes 45s.
 */
fun GameState.afterBuyPotion(id: String): GameState {
    val potion = Potions.byId(id) ?: return this
    val timeLeft = activePotions.firstOrNull { it.potionId == id }?.secondsLeft ?: 0
    return copy(
        activePotions = activePotions.filterNot { it.potionId == id } +
                ActivePotion(id, timeLeft + potion.durationSeconds),
        stats = stats.copy(potionsUsed = stats.potionsUsed + 1)
    )
}
/** One second passes: apprentices brew, potion and message timers count down. */
fun GameState.afterTick(): GameState {
    val auto = autoGain
    return copy(
        points = points + auto,
        stats = stats.copy(brewed = stats.brewed + auto),
        activePotions = activePotions
            .map { it.copy(secondsLeft = it.secondsLeft - 1) }
            .filter { it.secondsLeft > 0 },
        unlockNotice = unlockNotice?.let {
            if (it.secondsLeft <= 1) null else it.copy(secondsLeft = it.secondsLeft - 1)
        }
    )
}

/** Removes an owned achievement. Progress restarts from zero, so it can be earned again. */
fun GameState.afterDeleteAchievement(id: String): GameState {
    val achievement = Achievements.all.firstOrNull { it.id == id }
    return if (achievement == null || id !in ownedAchievements) this else copy(
        ownedAchievements = ownedAchievements - id,
        baselines = baselines + (id to achievement.requirement.stat(stats))
    )
}

fun GameState.afterBuyExtraHand(): GameState =
    if (points < extraClickCost) this else copy(
        points = points - extraHandCost,
        extraHandCost = extraHandCost + (extraHandCost * GrowthFactorExtraHand).roundToInt(),
        extraHandCount = extraHandCount + 1
    )

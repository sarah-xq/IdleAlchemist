package com.example.idlealchemist.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import idlealchemist.shared.generated.resources.Res
import idlealchemist.shared.generated.resources.icon_stir
import idlealchemist.shared.generated.resources.icon_apprentice

/**
 * Stronger stir upgrade.
 *
 * @param cost Price of the next level.
 * @param points The player's current points (enables the button and fills the bar).
 * @param clickPower Current points per click.
 * @param onClick Called when the player taps Buy.
 */
@Composable
fun ExtraClickUpgrade(
    modifier: Modifier = Modifier,
    cost: Int,
    points: Int,
    clickPower: Int,
    onClick: () -> Unit
) {
    UpgradeCard(
        icon = painterResource(Res.drawable.icon_stir),
        name = "Stronger Stir",
        description = "+$clickPower per tap",
        level = clickPower - 1,
        cost = cost,
        points = points,
        actionLabel = "Buy",
        onClick = onClick,
        modifier = modifier
    )
}

/**
 * Apprentice upgrade.
 *
 * @param cost Price of the next apprentice.
 * @param points The player's current points (enables the button and fills the bar).
 * @param apprenticeCount Number of apprentices owned.
 * @param onClick Called when the player taps Hire.
 */
@Composable
fun ApprenticeUpgrade(
    modifier: Modifier = Modifier,
    cost: Int,
    points: Int,
    apprenticeCount: Int,
    onClick: () -> Unit
) {
    UpgradeCard(
        icon = painterResource(Res.drawable.icon_apprentice),
        name = "Apprentice",
        description = "+$apprenticeCount per second",
        level = apprenticeCount,
        cost = cost,
        points = points,
        actionLabel = "Hire",
        onClick = onClick,
        modifier = modifier
    )
}

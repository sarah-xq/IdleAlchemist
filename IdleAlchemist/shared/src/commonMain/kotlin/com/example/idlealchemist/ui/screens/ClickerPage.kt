package com.example.idlealchemist.ui.screens

import com.example.idlealchemist.theme.ClickerTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.model.GameState
import com.example.idlealchemist.ui.components.ActivePotionsRow
import com.example.idlealchemist.ui.components.ApprenticeUpgrade
import com.example.idlealchemist.ui.components.ClickArea
import com.example.idlealchemist.ui.components.DisplayCurrentPoints
import com.example.idlealchemist.ui.components.ExtraClickUpgrade
import com.example.idlealchemist.ui.components.UnlockBanner
import com.example.idlealchemist.ui.components.UpgradeCard
import org.jetbrains.compose.resources.DrawableResource
import idlealchemist.shared.generated.resources.Res
import idlealchemist.shared.generated.resources.icon_stir
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.lazy.items
import idlealchemist.shared.generated.resources.icon_apprentice

data class Upgrade(
    val id: Int,
    val icon: DrawableResource,
    val name: String,
    val description: String,
    val level: Int,
    val cost: Int,
    val actionLevel: String,
    val onClick: () -> Unit
)

fun upgrade(
    state: GameState,
    onBuyExtraClick: () -> Unit,
    onBuyApprentice: () -> Unit,
) : List<Upgrade> = listOf(
    Upgrade(
       id = 0,
        icon = Res.drawable.icon_stir,
        name = "Stronger Grip",
        description = "+ ${state.clickPower} per tap",
        level = state.clickPower - 1,
        cost = state.extraClickCost,
        actionLevel = "Tap",
        onClick = onBuyExtraClick
    ),
    Upgrade(
        id = 1,
        icon = Res.drawable.icon_apprentice,
        name = "Apprentice",
        description = "+ ${state.apprenticeCount} per second",
        level = state.apprenticeCount,
        cost = state.autoCost,
        actionLevel = "Recruit",
        onClick = onBuyApprentice
    ),
    Upgrade(
        id = 2,
        icon = Res.drawable.icon_apprentice,
        name = "Apprentice",
        description = "+ ${state.apprenticeCount} per second",
        level = state.apprenticeCount,
        cost = state.autoCost,
        actionLevel = "Recruit",
        onClick = onBuyApprentice
)
)

/**
 * Main page for the Clicker Game. Stateless: the game lives in GameRepository, this page only
 * draws [state] and reports taps and purchases upward.
 *
 * AI -> restyled the layout with the alchemy theme, moved the game state into GameRepository so the
 * achievements and shop screens can share it, all original game logic was written by me
 */
@Composable
fun ClickerPage(
    state: GameState,
    onTap: () -> Unit,
    onBuyExtraClick: () -> Unit,
    onBuyApprentice: () -> Unit,
    modifier: Modifier = Modifier
) {

    val upgrades = upgrade(state, onBuyExtraClick, onBuyApprentice)
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            DisplayCurrentPoints(state.points, state.autoGain)

            state.unlockNotice?.let { UnlockBanner(it, Modifier.padding(top = 6.dp)) }
            if (state.activePotions.isNotEmpty()) ActivePotionsRow(state.activePotions)

            ClickArea(clickPower = state.clickGain, onClick = onTap)

            Text(
                text = "UPGRADES",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = ClickerTheme.colors.vellum,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 200.dp)
            ) {
                items(upgrades, key = { it.id }) { upgrade ->
                    UpgradeCard(
                        icon = painterResource(upgrade.icon),
                        name = upgrade.name,
                        description = upgrade.description,
                        level = upgrade.level,
                        cost = upgrade.cost,
                        points = state.points,
                        actionLabel = upgrade.actionLevel,
                        onClick = upgrade.onClick
                    )
                }
            }
        }
    }
}

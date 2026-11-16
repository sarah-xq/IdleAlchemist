package com.example.idlealchemist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import idlealchemist.shared.generated.resources.Res
import idlealchemist.shared.generated.resources.icon_potion
import com.example.idlealchemist.model.GameState
import com.example.idlealchemist.model.Potions
import com.example.idlealchemist.ui.components.GooglePaySheet
import com.example.idlealchemist.ui.components.PurchaseCard
import com.example.idlealchemist.ui.components.ScreenHeader
import com.example.idlealchemist.util.formatPrice
import org.jetbrains.compose.resources.painterResource

/**
 * Shop screen: potions that temporarily boost clicks or apprentices, "bought" with a mock
 * Google Pay checkout. Tapping a price opens the checkout popup; the potion is only handed out
 * (through [onBuy]) when the fake payment succeeds.
 *
 * Stateful only for which potion's checkout is open; the game data comes in as [state].
 * [subtitle] says which game the potions boost.
 */
@Composable
fun ShopScreen(
    state: GameState,
    onBuy: (potionId: String) -> Unit,
    subtitle: String = "Boost your brew",
    modifier: Modifier = Modifier
) {
    var pendingId by rememberSaveable { mutableStateOf<String?>(null) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScreenHeader("POTION SHOP", subtitle)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(Potions.all) { potion ->
                    val active = state.activePotions.firstOrNull { it.potionId == potion.id }
                    PurchaseCard(
                        icon = painterResource(Res.drawable.icon_potion),
                        name = potion.name,
                        description = potion.description,
                        price = formatPrice(potion.priceCents),
                        actionLabel = if (active != null) "Refill" else "Buy",
                        statusText = active?.let { "Active: ${it.secondsLeft}s left" },
                        onClick = { pendingId = potion.id }
                    )
                }
            }
        }
    }

    pendingId?.let { id ->
        Potions.byId(id)?.let { potion ->
            GooglePaySheet(
                itemName = potion.name,
                itemDescription = potion.description,
                price = formatPrice(potion.priceCents),
                icon = painterResource(Res.drawable.icon_potion),
                onPaid = { onBuy(potion.id) },
                onDismiss = { pendingId = null }
            )
        }
    }
}

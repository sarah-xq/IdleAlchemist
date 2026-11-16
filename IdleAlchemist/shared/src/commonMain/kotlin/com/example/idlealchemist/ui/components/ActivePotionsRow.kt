package com.example.idlealchemist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.model.ActivePotion
import com.example.idlealchemist.model.Potions
import com.example.idlealchemist.theme.ClickerTheme

/** Gold chips showing each running potion and its remaining seconds. Stateless. */
@Composable
fun ActivePotionsRow(potions: List<ActivePotion>, modifier: Modifier = Modifier) {
    val c = ClickerTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        potions.forEach { active ->
            val name = Potions.byId(active.potionId)?.name ?: active.potionId
            Text(
                text = "$name  ${active.secondsLeft}s",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = c.onGold,
                modifier = Modifier
                    .background(c.gold, RoundedCornerShape(4.dp))
                    .border(2.dp, c.goldDark, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

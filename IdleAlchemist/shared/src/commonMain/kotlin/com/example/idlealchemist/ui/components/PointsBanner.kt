package com.example.idlealchemist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.ui.components.chunky
import com.example.idlealchemist.theme.ClickerTheme
import com.example.idlealchemist.util.formatCount

/**
 * Steel banner at the top: current points plus points per second. Stateless.
 *
 * @param clickCount Current essence.
 * @param perSecond Points the apprentices earn each second (potions included).
 */
@Composable
fun DisplayCurrentPoints(
    clickCount: Int,
    perSecond: Int,
    modifier: Modifier = Modifier
) {
    val c = ClickerTheme.colors
    Column(
        modifier = modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .chunky(c.panel, c.panelEdge)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("CURRENT ESSENCE", style = MaterialTheme.typography.labelSmall, color = ClickerTheme.colors.vellum)
        Text(
            text = formatCount(clickCount),
            style = MaterialTheme.typography.displayLarge.copy(shadow = Shadow(c.goldDark, Offset(0f, 6f), 0f)),
            color = c.gold
        )
        Text("$perSecond per second", style = MaterialTheme.typography.bodySmall, color = ClickerTheme.colors.vellum)
    }
}

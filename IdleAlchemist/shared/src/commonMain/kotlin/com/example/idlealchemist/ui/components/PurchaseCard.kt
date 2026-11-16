package com.example.idlealchemist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idlealchemist.theme.ClickerTheme

/**
 * A shop row for something bought with real-looking money: icon, name, description, an optional
 * status tag (e.g. "Active: 12s left") and a price button. Stateless.
 */
@Composable
fun PurchaseCard(
    icon: Painter,
    name: String,
    description: String,
    price: String,
    actionLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    statusText: String? = null
) {
    val c = ClickerTheme.colors
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .chunky(colors.surface, c.panelEdge)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .background(c.accent, RoundedCornerShape(4.dp))
                .border(3.dp, c.accentEdge, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(icon, contentDescription = null, modifier = Modifier.size(34.dp))
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(name, style = MaterialTheme.typography.titleMedium, color = colors.onSurface)
            Text(description, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
            if (statusText != null) {
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = c.onGold,
                    modifier = Modifier
                        .background(c.gold, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .widthIn(min = 76.dp)
                .chunky(c.panelEdge, c.gold, radius = 4.dp)
                .clickable { onClick() }
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(actionLabel, fontSize = 11.sp, color = ClickerTheme.colors.vellum)
            Text(price, style = MaterialTheme.typography.labelLarge, color = c.gold)
        }
    }
}

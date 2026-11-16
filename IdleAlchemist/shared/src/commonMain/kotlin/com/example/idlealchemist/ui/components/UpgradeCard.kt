package com.example.idlealchemist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idlealchemist.theme.ClickerTheme
import com.example.idlealchemist.util.formatCount

/**
 * Adventure Capitalist style row: icon with optional level badge, name, description, a bar showing
 * how close the player is to affording it, and a Buy button that greys out when unaffordable.
 * Used for both upgrades and shop potions. Stateless.
 *
 * @param level Number shown in the gold badge, or null for no badge (potions).
 */
@Composable
fun UpgradeCard(
    icon: Painter,
    name: String,
    description: String,
    level: Int?,
    cost: Int,
    points: Int,
    actionLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = ClickerTheme.colors
    val colors = MaterialTheme.colorScheme
    val canBuy = points >= cost
    val progress = (points.toFloat() / cost.toFloat()).coerceIn(0f, 1f)

    Row(
        modifier = modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .chunky(colors.surface, c.panelEdge)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(c.accent, RoundedCornerShape(4.dp))
                    .border(3.dp, c.accentEdge, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(icon, contentDescription = null, modifier = Modifier.size(34.dp))
            }
            if (level != null) {
                Text(
                    text = "$level",
                    color = c.onGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 6.dp, y = 4.dp)
                        .background(c.gold, RoundedCornerShape(4.dp))
                        .border(2.dp, c.goldDark, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp)
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(name, style = MaterialTheme.typography.titleMedium, color = colors.onSurface)
            Text(description, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(colors.surfaceVariant, RoundedCornerShape(4.dp))
                    .border(2.dp, c.panelEdge, RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(12.dp)
                        .background(
                            Brush.verticalGradient(listOf(c.gold, c.goldDark)),
                            RoundedCornerShape(4.dp)
                        )
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .widthIn(min = 84.dp)
                .chunky(
                    face = if (canBuy) c.panelEdge else c.disabledFace,
                    edge = if (canBuy) c.gold else c.disabledEdge,
                    radius = 4.dp
                )
                .clickable(enabled = canBuy) { onClick() }
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(actionLabel, fontSize = 11.sp, color = ClickerTheme.colors.vellum)
            Text(
                text = formatCount(cost),
                style = MaterialTheme.typography.labelLarge,
                color = if (canBuy) c.gold else c.disabledText
            )
        }
    }
}

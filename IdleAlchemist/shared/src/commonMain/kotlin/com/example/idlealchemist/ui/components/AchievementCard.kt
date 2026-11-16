package com.example.idlealchemist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idlealchemist.ui.components.chunky
import com.example.idlealchemist.model.AchievementStatus
import com.example.idlealchemist.theme.ClickerTheme

/**
 * One achievement: badge, title, description, owned/locked tag and a progress bar. Tapping expands
 * it in place to show exact progress and, for owned ones, a Delete button. Stateless.
 */
@Composable
fun AchievementCard(
    status: AchievementStatus,
    expanded: Boolean,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = ClickerTheme.colors
    val colors = MaterialTheme.colorScheme
    val a = status.achievement
    val progress = status.progress.toFloat() / a.requirement.target

    Column(
        modifier = modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .chunky(colors.surface, c.panelEdge)
            .clickable(onClick = onToggle)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(if (status.owned) c.gold else c.disabledFace, RoundedCornerShape(4.dp))
                    .border(3.dp, if (status.owned) c.goldDark else c.disabledEdge, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(if (status.owned) "\u2605" else "?", fontSize = 20.sp, color = if (status.owned) c.onGold else c.disabledText)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(a.title, style = MaterialTheme.typography.titleMedium, color = colors.onSurface)
                Text(a.description, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
            }
            Text(
                text = if (status.owned) "OWNED" else "LOCKED",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = if (status.owned) c.onGold else c.vellum,
                modifier = Modifier
                    .background(if (status.owned) c.cyan else c.disabledEdge, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }

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

        if (expanded) {
            Text(
                text = "Progress: ${status.progress} / ${a.requirement.target} ${a.requirement.unit}",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant
            )
            if (status.owned) {
                Button(onClick = onDelete) { Text("Delete (earn it again)") }
            }
        }
    }
}

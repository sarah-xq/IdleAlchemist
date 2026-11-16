package com.example.idlealchemist.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.ui.components.chunky
import com.example.idlealchemist.model.UnlockNotice
import com.example.idlealchemist.theme.ClickerTheme

/** Gold "achievement unlocked" message that appears for a few seconds. Stateless. */
@Composable
fun UnlockBanner(notice: UnlockNotice, modifier: Modifier = Modifier) {
    val c = ClickerTheme.colors
    Text(
        text = "ACHIEVEMENT UNLOCKED: ${notice.title}",
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
        color = c.onGold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .chunky(c.gold, c.goldDark, radius = 4.dp)
            .padding(8.dp)
    )
}

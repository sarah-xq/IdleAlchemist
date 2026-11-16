package com.example.idlealchemist.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.theme.ClickerTheme

/** Steel title banner used at the top of the achievements and contact screens. Stateless. */
@Composable
fun ScreenHeader(title: String, subtitle: String? = null, modifier: Modifier = Modifier) {
    val c = ClickerTheme.colors
    Column(
        modifier = modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .chunky(c.panel, c.panelEdge)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.labelSmall, color = ClickerTheme.colors.vellum)
        if (subtitle != null) {
            Text(subtitle, style = MaterialTheme.typography.titleMedium, color = c.gold)
        }
    }
}

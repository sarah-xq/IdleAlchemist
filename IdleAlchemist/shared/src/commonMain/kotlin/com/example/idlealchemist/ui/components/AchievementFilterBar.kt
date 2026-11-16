package com.example.idlealchemist.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.ui.components.chunky
import com.example.idlealchemist.theme.ClickerTheme
import com.example.idlealchemist.util.AchievementSort

/**
 * The search form on the achievements screen: a text box (the keywords "owned" and "unowned" work
 * as filters), shortcut chips that fill in those keywords, and sort chips. Stateless.
 */
@Composable
fun AchievementFilterBar(
    query: String,
    onQueryChange: (String) -> Unit,
    sort: AchievementSort,
    onSortChange: (AchievementSort) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = ClickerTheme.colors
    Column(
        modifier = modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .chunky(MaterialTheme.colorScheme.surface, c.panelEdge)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("Search achievements") },
            placeholder = { Text("try: owned, unowned, click") }
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("owned", "unowned").forEach { word ->
                val selected = query.trim().equals(word, ignoreCase = true)
                FilterChip(
                    selected = selected,
                    onClick = { onQueryChange(if (selected) "" else word) },
                    label = { Text(word) }
                )
            }
            Text("Sort:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            AchievementSort.entries.forEach { option ->
                FilterChip(
                    selected = sort == option,
                    onClick = { onSortChange(option) },
                    label = { Text(option.label) }
                )
            }
        }
    }
}

package com.example.idlealchemist.ui.screens

import com.example.idlealchemist.theme.ClickerTheme
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.model.Achievements
import com.example.idlealchemist.model.GameState
import com.example.idlealchemist.model.achievementStatuses
import com.example.idlealchemist.ui.components.AchievementCard
import com.example.idlealchemist.ui.components.AchievementFilterBar
import com.example.idlealchemist.ui.components.ScreenHeader
import com.example.idlealchemist.util.AchievementSort
import com.example.idlealchemist.util.filterAchievements
import com.example.idlealchemist.util.sortAchievements

/**
 * Achievements screen: every goal in the game with the player's progress. The search box filters
 * (keywords "owned" / "unowned"), the chips sort, tapping a card expands it, and owned
 * achievements can be deleted and earned again.
 *
 * Stateful only for the screen's own UI state (search text, sort, which card is open, which delete
 * is awaiting confirmation); the game data comes in as [state].
 */
@Composable
fun AchievementsScreen(
    state: GameState,
    onDelete: (achievementId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by rememberSaveable { mutableStateOf("") }
    var sortIndex by rememberSaveable { mutableStateOf(0) }
    var expandedId by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingDeleteId by rememberSaveable { mutableStateOf<String?>(null) }

    val all = state.achievementStatuses()
    val visible = sortAchievements(
        filterAchievements(all, query),
        AchievementSort.entries[sortIndex]
    )

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScreenHeader("ACHIEVEMENTS", "${all.count { it.owned }} / ${all.size} owned")

            AchievementFilterBar(
                query = query,
                onQueryChange = { query = it },
                sort = AchievementSort.entries[sortIndex],
                onSortChange = { sortIndex = it.ordinal }
            )

            if (visible.isEmpty()) {
                Text(
                    "No achievements match \"$query\".",
                    style = MaterialTheme.typography.bodySmall,
                    color = ClickerTheme.colors.vellum,
                    modifier = Modifier.padding(8.dp)
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(visible, key = { it.achievement.id }) { status ->
                    AchievementCard(
                        status = status,
                        expanded = expandedId == status.achievement.id,
                        onToggle = {
                            expandedId = if (expandedId == status.achievement.id) null else status.achievement.id
                        },
                        onDelete = { pendingDeleteId = status.achievement.id }
                    )
                }
            }
        }
    }

    pendingDeleteId?.let { id ->
        val title = Achievements.all.firstOrNull { it.id == id }?.title ?: ""
        AlertDialog(
            onDismissRequest = { pendingDeleteId = null },
            title = { Text("Delete achievement?") },
            text = { Text("\"$title\" will be removed and you will have to earn it again from zero.") },
            confirmButton = {
                TextButton(onClick = { onDelete(id); pendingDeleteId = null }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteId = null }) { Text("Cancel") }
            }
        )
    }
}

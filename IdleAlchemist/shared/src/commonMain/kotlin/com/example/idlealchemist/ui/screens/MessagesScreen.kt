package com.example.idlealchemist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.model.ContactMessage
import com.example.idlealchemist.theme.ClickerTheme
import com.example.idlealchemist.ui.components.ScreenHeader
import com.example.idlealchemist.ui.components.chunky

/**
 * Screen 3 of the flow: every message sent so far, each with Details and Remove.
 * Remove asks for confirmation first. Stateless apart from the id waiting for confirmation,
 * which is kept with rememberSaveable so the dialog survives rotation.
 */
@Composable
fun MessagesScreen(
    messages: List<ContactMessage>,
    onOpen: (Int) -> Unit,
    onRemove: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pendingRemoveId by rememberSaveable { mutableStateOf<Int?>(null) }
    val c = ClickerTheme.colors

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier.widthIn(max = 520.dp).fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { ScreenHeader("SENT MESSAGES", "${messages.size} sent") }

            if (messages.isEmpty()) {
                item {
                    Text(
                        "Nothing sent yet. Go back and send your first message!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClickerTheme.colors.vellum
                    )
                }
            }

            items(messages, key = { it.id }) { m ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .chunky(MaterialTheme.colorScheme.surface, c.panelEdge)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        m.subject.ifBlank { "(no subject)" },
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "From ${m.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(m.message, color = Color.White, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { onOpen(m.id) }) { Text("Details") }
                        OutlinedButton(onClick = { pendingRemoveId = m.id }) { Text("Remove") }
                    }
                }
            }

            item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") } }
        }
    }

    pendingRemoveId?.let { id ->
        AlertDialog(
            onDismissRequest = { pendingRemoveId = null },
            title = { Text("Remove message?") },
            text = { Text("This message will be removed from your sent list.") },
            confirmButton = {
                TextButton(onClick = { onRemove(id); pendingRemoveId = null }) { Text("Remove") }
            },
            dismissButton = { TextButton(onClick = { pendingRemoveId = null }) { Text("Cancel") } }
        )
    }
}

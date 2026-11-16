package com.example.idlealchemist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.model.ContactMessage
import com.example.idlealchemist.theme.ClickerTheme
import com.example.idlealchemist.ui.components.RemoteImage
import com.example.idlealchemist.ui.components.ScreenHeader
import com.example.idlealchemist.ui.components.chunky

/**
 * Screen 2 of the flow: shows ONE message. The message id arrives as a navigation parameter
 * (Route.MessageDetail), and the caller looks the message up. Stateless.
 *
 * @param message the message to show, or null if it no longer exists (for example it was removed).
 */
@Composable
fun MessageDetailScreen(
    message: ContactMessage?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val c = ClickerTheme.colors
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScreenHeader("MESSAGE", message?.subject?.ifBlank { "(no subject)" } ?: "Not found")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .chunky(MaterialTheme.colorScheme.surface, c.panelEdge)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (message == null) {
                    Text("This message no longer exists.", color = Color.White)
                } else {
                    Text("From: ${message.name} <${message.email}>", style = MaterialTheme.typography.bodySmall, color = Color.White)
                    Text(message.message, color = Color.White)
                    if (message.screenshotLink.isNotBlank()) {
                        Text("Screenshot", style = MaterialTheme.typography.labelMedium, color = Color.White)
                        RemoteImage(
                            url = message.screenshotLink,
                            contentDescription = "Screenshot attached to the message",
                            modifier = Modifier.fillMaxWidth().height(200.dp)
                        )
                    }
                }
            }

            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
        }
    }
}

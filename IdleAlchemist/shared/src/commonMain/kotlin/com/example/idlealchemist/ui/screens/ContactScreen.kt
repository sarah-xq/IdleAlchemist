package com.example.idlealchemist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.theme.ClickerTheme
import com.example.idlealchemist.ui.components.RemoteImage
import com.example.idlealchemist.ui.components.ScreenHeader
import com.example.idlealchemist.ui.components.chunky

/**
 * Contact Us screen (screen 1 of the flow): a message form with several text fields and an
 * optional screenshot link. Send is a mock (nothing leaves the device). After sending, Preview
 * opens the message on its own screen, and "Sent messages" opens the full list.
 *
 * Form text lives here with rememberSaveable so half-typed text survives rotation.
 *
 * @param sentCount how many messages have been sent so far (shown on the list button).
 * @param onSend stores the message and returns its new id.
 * @param onPreview open the message with that id on the detail screen.
 * @param onOpenList open the list of sent messages.
 */
@Composable
fun ContactScreen(
    sentCount: Int,
    onSend: (name: String, email: String, subject: String, message: String, screenshotLink: String) -> Int,
    onPreview: (Int) -> Unit,
    onOpenList: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var subject by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }
    var screenshotLink by rememberSaveable { mutableStateOf("") }
    var lastSentId by rememberSaveable { mutableStateOf<Int?>(null) }
    var showErrors by rememberSaveable { mutableStateOf(false) }
    var showPreview by rememberSaveable { mutableStateOf(false) }

    val nameOk = name.isNotBlank()
    val emailOk = '@' in email && '.' in email.substringAfter('@')
    val messageOk = message.isNotBlank()

    // Sends the message and clears the form. Used by both the Send button and the preview dialog.
    fun submit() {
        lastSentId = onSend(name, email, subject, message, screenshotLink)
        name = ""; email = ""; subject = ""; message = ""; screenshotLink = ""
        showErrors = false
        showPreview = false
    }

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
            ScreenHeader("CONTACT US", "We would love to hear from you")

            Column(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .fillMaxWidth()
                    .chunky(MaterialTheme.colorScheme.surface, c.panelEdge)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FormField(name, { name = it; lastSentId = null }, "Your name", showErrors && !nameOk, "Please enter your name")
                FormField(email, { email = it; lastSentId = null }, "Your email", showErrors && !emailOk, "Enter a valid email address")
                FormField(subject, { subject = it; lastSentId = null }, "Subject")
                FormField(
                    message, { message = it; lastSentId = null }, "Message",
                    showErrors && !messageOk, "Please write a message", singleLine = false
                )
                FormField(
                    screenshotLink, { screenshotLink = it; lastSentId = null },
                    "Screenshot link (optional)"
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Preview comes before sending: check the message first, then send or edit.
                    OutlinedButton(onClick = {
                        showErrors = true
                        if (nameOk && emailOk && messageOk) showPreview = true
                    }) { Text("Preview") }

                    Button(onClick = {
                        showErrors = true
                        if (nameOk && emailOk && messageOk) submit()
                    }) { Text("Send") }
                }

                // After sending, the message can be opened on its own screen.
                lastSentId?.let { id ->
                    OutlinedButton(onClick = { onPreview(id) }) { Text("View sent message") }
                }

                if (lastSentId != null) {
                    Text(
                        "Message sent! (Demo only: nothing leaves your device.)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            OutlinedButton(onClick = onOpenList, modifier = Modifier.fillMaxWidth()) {
                Text("Sent messages ($sentCount)")
            }
        }
    }

    if (showPreview) {
        AlertDialog(
            onDismissRequest = { showPreview = false },
            title = { Text(subject.ifBlank { "(no subject)" }) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("From: $name <$email>", style = MaterialTheme.typography.bodySmall)
                    Text(message)
                    if (screenshotLink.isNotBlank()) {
                        RemoteImage(
                            url = screenshotLink,
                            contentDescription = "Screenshot attached to the message",
                            modifier = Modifier.fillMaxWidth().height(160.dp)
                        )
                    }
                }
            },
            confirmButton = { TextButton(onClick = { submit() }) { Text("Send") } },
            dismissButton = { TextButton(onClick = { showPreview = false }) { Text("Edit") } }
        )
    }
}

/** One labelled text box with an optional error message. Stateless. */
@Composable
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorText: String = "",
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
        isError = isError,
        supportingText = if (isError) ({ Text(errorText) }) else null
    )
}

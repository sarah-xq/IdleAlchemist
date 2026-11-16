package com.example.idlealchemist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.idlealchemist.theme.AbyssalSlate
import com.example.idlealchemist.theme.AetherCyan
import com.example.idlealchemist.theme.AgedVellum
import com.example.idlealchemist.theme.DustyAsh
import com.example.idlealchemist.theme.ForgedSteel
import com.example.idlealchemist.theme.IronKeep
import com.example.idlealchemist.theme.WitchHazel
import com.example.idlealchemist.theme.WornLeather
import kotlinx.coroutines.delay

private val SheetInk = AgedVellum
private val SheetGray = DustyAsh
private val SheetLine = ForgedSteel
private val SheetBlue = WitchHazel
private val SheetGreen = AetherCyan

private const val STAGE_CONFIRM = 0
private const val STAGE_PROCESSING = 1
private const val STAGE_SUCCESS = 2

/**
 * A mock "Google Pay" checkout popup. Nothing real happens: no card, no network, no money.
 * It walks through three stages (confirm, processing, success) and calls [onPaid] once, at the
 * moment the fake payment succeeds. The dialog cannot be dismissed while processing.
 *
 * @param onPaid Called when the fake payment succeeds; the caller hands out the purchase here.
 * @param onDismiss Called when the player cancels or taps Done.
 */
@Composable
fun GooglePaySheet(
    itemName: String,
    itemDescription: String,
    price: String,
    icon: Painter,
    onPaid: () -> Unit,
    onDismiss: () -> Unit
) {
    var stage by rememberSaveable { mutableStateOf(STAGE_CONFIRM) }

    LaunchedEffect(stage) {
        if (stage == STAGE_PROCESSING) {
            delay(1500L)
            onPaid()
            stage = STAGE_SUCCESS
        }
    }

    Dialog(onDismissRequest = { if (stage != STAGE_PROCESSING) onDismiss() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(IronKeep, RoundedCornerShape(4.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Google Pay  \u00B7  demo", fontSize = 13.sp, color = SheetGray, fontWeight = FontWeight.Medium)

            when (stage) {
                STAGE_CONFIRM -> {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Image(icon, contentDescription = null, modifier = Modifier.size(40.dp))
                        Column(Modifier.weight(1f)) {
                            Text(itemName, color = SheetInk, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(itemDescription, color = SheetGray, fontSize = 12.sp)
                            Text("Clicker Adventure", color = SheetGray, fontSize = 12.sp)
                        }
                        Text(price, color = SheetInk, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Hairline()
                    DetailRow("Pay with", "Visa \u2022\u2022\u2022\u2022 4242")
                    DetailRow("Account", "you@example.com")
                    Hairline()
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", color = SheetInk, fontWeight = FontWeight.Bold)
                        Text(price, color = SheetInk, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { stage = STAGE_PROCESSING },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SheetBlue, contentColor = AbyssalSlate)
                    ) { Text("Pay ") }
                    Text(
                        "Demo only. No real payment is taken.",
                        color = SheetGray, fontSize = 11.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = WornLeather, contentColor = AgedVellum)
                    ) { Text("Cancel") }
                }

                STAGE_PROCESSING -> {
                    Column(Modifier.fillMaxWidth().padding(vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = SheetBlue)
                        Spacer(Modifier.height(16.dp))
                        Text("Processing payment...", color = SheetInk)
                    }
                }

                else -> {
                    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.size(56.dp).background(SheetGreen, RoundedCornerShape(4.dp)), contentAlignment = Alignment.Center) {
                            Text("\u2713", color = AbyssalSlate, fontSize = 30.sp)
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("Payment successful", color = SheetInk, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("$itemName is now active", color = SheetGray, fontSize = 13.sp)
                    }
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SheetBlue, contentColor = AbyssalSlate)
                    ) { Text("Done") }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = SheetGray, fontSize = 13.sp)
        Text(value, color = SheetInk, fontSize = 13.sp)
    }
}

@Composable
private fun Hairline() {
    Box(Modifier.fillMaxWidth().height(1.dp).background(SheetLine))
}

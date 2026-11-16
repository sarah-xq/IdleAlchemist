package com.example.idlealchemist.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.theme.ClickerTheme

/** One floating "+N" number. Rises and fades over 800 ms, then calls [onDone] so the caller can remove it. */
data class Pop(val id: Long, val amount: Int, val dx: Int)

@Composable
fun FloatingPop(pop: Pop, onDone: () -> Unit) {
    val c = ClickerTheme.colors
    val progress = remember { Animatable(0f) }
    LaunchedEffect(pop.id) {
        progress.animateTo(1f, tween(800))
        onDone()
    }
    Text(
        text = "+${pop.amount}",
        style = MaterialTheme.typography.headlineSmall.copy(shadow = Shadow(c.goldDark, Offset(0f, 4f), 0f)),
        color = c.gold,
        modifier = Modifier
            .offset(x = pop.dx.dp, y = (-30 - 90 * progress.value).dp)
            .alpha(1f - progress.value)
    )
}

package com.example.idlealchemist.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.theme.ClickerTheme
import com.example.idlealchemist.ui.components.FloatingPop
import com.example.idlealchemist.ui.components.Pop
import org.jetbrains.compose.resources.painterResource
import idlealchemist.shared.generated.resources.Res
import idlealchemist.shared.generated.resources.cauldron

/**
 * The big tappable image in the middle of the page. Shrinks while pressed and spawns floating
 * "+N" numbers on every tap.
 *
 * @param clickPower Points gained per click (shown in the floating numbers).
 * @param onClick Called on every tap.
 * @param image What to tap. Defaults to the cauldron; later this can be the user's own image.
 */
@Composable
fun ColumnScope.ClickArea(
    modifier: Modifier = Modifier,
    clickPower: Int,
    onClick: () -> Unit,
    image: Painter = painterResource(Res.drawable.cauldron)
) {
    val pops = remember { mutableStateListOf<Pop>() }
    var nextPopId by remember { mutableStateOf(0L) }
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.94f else 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = image,
            contentDescription = "Tap to brew",
            modifier = Modifier
                .size(220.dp)
                .scale(scale)
                .clickable(interactionSource = interaction, indication = null) {
                    onClick()
                    pops.add(Pop(nextPopId++, clickPower, (-40..40).random()))
                }
        )

        for (pop in pops.toList()) {
            key(pop.id) { FloatingPop(pop) { pops.remove(pop) } }
        }

        Text(
            text = "Click to Brew!",
            style = MaterialTheme.typography.bodySmall,
            color = ClickerTheme.colors.vellum,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

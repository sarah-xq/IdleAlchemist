package com.example.idlealchemist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Cartoon "Adventure Capitalist" look: coloured face, dark border and a solid ledge underneath.
 * The ledge is drawn below the component's bounds, so callers add [depth] of bottom padding.
 */
fun Modifier.chunky(
    face: Color,
    edge: Color,
    radius: Dp = 6.dp,
    depth: Dp = 4.dp
): Modifier {
    val shape = RoundedCornerShape(radius)
    return this
        .drawBehind {
            drawRoundRect(
                color = edge,
                topLeft = Offset(0f, depth.toPx()),
                size = size,
                cornerRadius = CornerRadius(radius.toPx())
            )
        }
        .background(face, shape)
        .border(3.dp, edge, shape)
}


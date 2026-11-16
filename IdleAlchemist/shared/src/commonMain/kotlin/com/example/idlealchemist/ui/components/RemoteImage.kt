package com.example.idlealchemist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import idlealchemist.shared.generated.resources.Res
import idlealchemist.shared.generated.resources.cauldron
import org.jetbrains.compose.resources.painterResource

/**
 * Loads an image from a web link with Coil and always shows something sensible:
 * the default cauldron while loading, and the same cauldron if the link is blank, wrong or offline.
 * Stateless: Coil handles loading and caching itself.
 *
 * @param url The image link the player typed. Blank means "no image".
 * @param contentDescription Text for screen readers.
 */
@Composable
fun RemoteImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val fallback = painterResource(Res.drawable.cauldron)

    if (url.isBlank()) {
        Image(fallback, contentDescription, modifier, contentScale = contentScale)
    } else {
        AsyncImage(
            model = url.trim(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
            placeholder = fallback,
            error = fallback
        )
    }
}

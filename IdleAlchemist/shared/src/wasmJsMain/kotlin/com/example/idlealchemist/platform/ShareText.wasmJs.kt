package com.example.idlealchemist.platform

import androidx.compose.runtime.Composable

/**
 * Web: share the text from the browser.
 *
 * TODO (web, student): make the returned function share or copy the text.
 *  - Simplest: copy it with navigator.clipboard.writeText(text), through a js(...) helper.
 *  - Nicer on phones: use navigator.share({ text }) when it exists, otherwise fall back to the clipboard.
 */
@Composable
actual fun rememberShareText(): (text: String) -> Unit = { text ->
    // TODO (web, student): share or copy text.
}

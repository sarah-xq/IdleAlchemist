package com.example.idlealchemist.platform

import androidx.compose.runtime.Composable
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

// Written with Claude (AI): Assignment 4 Part B, complex approach.

/** Desktop: copies the text to the system clipboard so it can be pasted anywhere. */
@Composable
actual fun rememberShareText(): (text: String) -> Unit = { text ->
    Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(text), null)
}

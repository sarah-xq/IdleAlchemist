package com.example.idlealchemist.platform

import androidx.compose.runtime.Composable

// Written with Claude (AI): Assignment 4 Part B, complex approach.

/**
 * Returns a function that shares a line of text, e.g. "I cleared Stage 3 in Idle Alchemist!".
 *
 * Android opens the system share sheet, desktop copies to the clipboard, and the web
 * uses the browser's share or clipboard API.
 */
@Composable
expect fun rememberShareText(): (text: String) -> Unit

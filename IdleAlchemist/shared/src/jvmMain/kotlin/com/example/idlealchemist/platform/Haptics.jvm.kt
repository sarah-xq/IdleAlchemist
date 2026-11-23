package com.example.idlealchemist.platform

import androidx.compose.runtime.Composable

// Written with Claude (AI): Assignment 4 Part B, complex approach.

/** Desktop: computers have no vibration motor, so this does nothing. */
@Composable
actual fun rememberHaptics(): (durationMillis: Long) -> Unit = { }

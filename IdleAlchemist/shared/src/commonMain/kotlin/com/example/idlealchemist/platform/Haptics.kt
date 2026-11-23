package com.example.idlealchemist.platform

import androidx.compose.runtime.Composable

// Written with Claude (AI): Assignment 4 Part B, complex approach.

/**
 * Returns a function that gives a short vibration, e.g. when the crucible is tapped.
 *
 * Each platform vibrates differently (Android's Vibrator service, the browser's
 * navigator.vibrate), so the implementation is an expect/actual.
 */
@Composable
expect fun rememberHaptics(): (durationMillis: Long) -> Unit

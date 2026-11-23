package com.example.idlealchemist.platform

import androidx.compose.runtime.Composable

/**
 * Web: vibrate the device through the browser.
 *
 * TODO (web, student): make the returned function call the browser's navigator.vibrate(durationMillis).
 *  - Kotlin/Wasm can't call navigator.vibrate directly; write a small top-level helper with js(...),
 *    e.g. a private fun that takes an Int and runs "navigator.vibrate(ms)".
 *  - Browsers without vibration (desktop Chrome, iOS Safari) simply ignore it.
 */
@Composable
actual fun rememberHaptics(): (durationMillis: Long) -> Unit = { durationMillis ->
    // TODO (web, student): vibrate for durationMillis.
}

package com.example.idlealchemist

import androidx.compose.runtime.Composable

@Composable
actual fun rememberResultResponder(): (resultData: String) -> Unit {
    return {}
}

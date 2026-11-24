package com.example.idlealchemist
import androidx.compose.runtime.Composable

@Composable
expect fun rememberResultResponder(): (resultData: String) -> Unit
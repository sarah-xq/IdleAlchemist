package com.example.idlealchemist.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.util.prefs.Preferences

// Written with Claude (AI): Assignment 4 Part B, complex approach.

/** Desktop: saves values with Java's Preferences (the registry on Windows). */
@Composable
actual fun rememberKeyValueStore(): KeyValueStore = remember {
    val prefs = Preferences.userRoot().node("idlealchemist")
    object : KeyValueStore {
        override fun getString(key: String): String? = prefs.get(key, null)
        override fun putString(key: String, value: String) {
            prefs.put(key, value)
        }
    }
}

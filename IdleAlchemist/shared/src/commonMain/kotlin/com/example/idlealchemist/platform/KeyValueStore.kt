package com.example.idlealchemist.platform

import androidx.compose.runtime.Composable

// Written with Claude (AI): Assignment 4 Part B, complex approach.

/** Small text storage that survives closing the app. */
interface KeyValueStore {
    /** Returns the saved value for [key], or null if nothing was saved. */
    fun getString(key: String): String?

    /** Saves [value] under [key], replacing any older value. */
    fun putString(key: String, value: String)
}

/**
 * Returns this device's [KeyValueStore].
 *
 * Android uses SharedPreferences, desktop uses java.util.prefs, and the web uses localStorage.
 */
@Composable
expect fun rememberKeyValueStore(): KeyValueStore

package com.example.idlealchemist.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Web: save values in the browser.
 *
 * TODO (web, student): store values in localStorage so they survive a page reload.
 *  - Read:  kotlinx.browser.localStorage.getItem(key)
 *  - Write: kotlinx.browser.localStorage.setItem(key, value)
 *  Until then nothing is saved, so the visit counter on the Codex page stays at 1 on web.
 */
@Composable
actual fun rememberKeyValueStore(): KeyValueStore = remember {
    object : KeyValueStore {
        override fun getString(key: String): String? = null // TODO (web, student): read from localStorage
        override fun putString(key: String, value: String) {
            // TODO (web, student): write to localStorage
        }
    }
}

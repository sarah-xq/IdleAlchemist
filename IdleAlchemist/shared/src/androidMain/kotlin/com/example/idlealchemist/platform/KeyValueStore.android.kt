package com.example.idlealchemist.platform

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberKeyValueStore(): KeyValueStore {
    val context = LocalContext.current
    return remember(context) {
        val prefs = context.getSharedPreferences("idlealchemist", Context.MODE_PRIVATE)
        object : KeyValueStore {
            override fun getString(key: String): String? = prefs.getString(key, null)
            override fun putString(key: String, value: String) {
                prefs.edit().putString(key, value).apply()
            }
        }
    }
}

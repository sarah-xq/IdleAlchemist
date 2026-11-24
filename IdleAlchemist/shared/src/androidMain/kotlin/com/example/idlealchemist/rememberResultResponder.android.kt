package com.example.idlealchemist

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable

@Composable
actual fun rememberResultResponder(): (resultData: String) -> Unit {
    val activity = LocalActivity.current

    return { resultData ->
        activity?.let { act ->
            val resultIntent = Intent().apply {
                putExtra("resultData", resultData)
            }
            act.setResult(Activity.RESULT_OK, resultIntent)
            act.finish()
        }
    }
}

package com.example.idlealchemist.platform

import android.os.Build

actual fun platformDetails(): String =
    "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT}) on ${Build.MANUFACTURER} ${Build.MODEL}"

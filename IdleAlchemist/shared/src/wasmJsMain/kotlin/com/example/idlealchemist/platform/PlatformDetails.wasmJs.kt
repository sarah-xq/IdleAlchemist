package com.example.idlealchemist.platform

/**
 * Web: describe the browser.
 *
 * TODO (web, student): return the browser's user agent, kotlinx.browser.window.navigator.userAgent,
 * optionally with the language from window.navigator.language.
 */
actual fun platformDetails(): String = "Web browser" // TODO (web, student): use navigator.userAgent

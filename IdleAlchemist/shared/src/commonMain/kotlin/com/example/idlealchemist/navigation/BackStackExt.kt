package com.example.idlealchemist.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Replaces the whole back stack with [route]. The new route is added before the old ones are
 * removed, so the stack is never empty (an empty stack would crash NavDisplay).
 */
fun MutableList<NavKey>.replaceWith(route: Route) {
    add(route)
    while (size > 1) removeAt(0)
}

/**
 * Used by the navigation bar. Does nothing if the user taps the screen they are already on,
 * so tapping "Play" does not throw away the skin that was passed in.
 */
fun MutableList<NavKey>.navigateTopLevel(route: Route) {
    val current = lastOrNull()
    if (current != null && current::class == route::class) return
    replaceWith(route)
}

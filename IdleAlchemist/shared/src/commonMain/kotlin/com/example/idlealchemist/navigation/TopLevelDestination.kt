package com.example.idlealchemist.navigation

/**
 * One entry in the navigation bar / rail.
 * [glyph] is a placeholder text icon; swap it for an image later.
 */
data class TopLevelDestination(
    val route: Route,
    val label: String,
    val glyph: String
)

val topLevelDestinations = listOf(
    TopLevelDestination(Route.Clicker, "Play", "\u25CF"),
    TopLevelDestination(Route.Achievements, "Achievements", "\u2605"),
    TopLevelDestination(Route.Shop, "Shop", "$"),
    TopLevelDestination(Route.Contact, "Contact", "\u2709"),
)

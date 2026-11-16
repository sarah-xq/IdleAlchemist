package com.example.idlealchemist.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/** The one Material 3 scheme, mapped from the alchemy palette. */
val AlchemyColorScheme = darkColorScheme(
    primary = WitchHazel,
    onPrimary = AbyssalSlate,
    primaryContainer = ForgedSteel,
    onPrimaryContainer = AgedVellum,
    secondary = AetherCyan,
    onSecondary = AbyssalSlate,
    secondaryContainer = ForgedSteel,
    onSecondaryContainer = AgedVellum,
    tertiary = TransmutationGold,
    onTertiary = AbyssalSlate,
    error = DragonEmber,
    onError = AbyssalSlate,
    background = IronKeep,
    onBackground = AgedVellum,
    surface = IronKeep,
    onSurface = AgedVellum,
    surfaceVariant = AbyssalSlate,
    onSurfaceVariant = DustyAsh,
    surfaceContainerLowest = AbyssalSlate,
    surfaceContainerLow = IronKeep,
    surfaceContainer = IronKeep,
    surfaceContainerHigh = WornLeather,
    surfaceContainerHighest = WornLeather,
    outline = DustyAsh,
    outlineVariant = ForgedSteel,
)

/**
 * Game-specific colours that Material 3 has no slot for (chunky ledges, gold numbers,
 * the page gradient). Read them with ClickerTheme.colors.
 */
@Immutable
data class ClickerColors(
    val backgroundTop: Color,
    val backgroundBottom: Color,
    val panel: Color,
    val panelEdge: Color,
    val gold: Color,
    val goldDark: Color,
    val onGold: Color,
    val accent: Color,
    val accentEdge: Color,
    val cyan: Color,
    val vellum: Color,
    val disabledFace: Color,
    val disabledEdge: Color,
    val disabledText: Color,
)

val AlchemyColors = ClickerColors(
    backgroundTop = IronKeep, backgroundBottom = AbyssalSlate,
    panel = WornLeather, panelEdge = ForgedSteel, gold = TransmutationGold, goldDark = DragonEmber,
    onGold = AbyssalSlate, accent = WitchHazel, accentEdge = ForgedSteel,
    cyan = AetherCyan, vellum = AgedVellum,
    disabledFace = WornLeather, disabledEdge = ForgedSteel, disabledText = DustyAsh,
)

package com.example.idlealchemist.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.idlealchemist.theme.AlchemyColorScheme
import com.example.idlealchemist.theme.AlchemyColors
import com.example.idlealchemist.theme.ClickerColors

private val LocalClickerColors = staticCompositionLocalOf { AlchemyColors }

/** Access point for the game-specific colours: ClickerTheme.colors.gold */
object ClickerTheme {
    val colors: ClickerColors
        @Composable @ReadOnlyComposable
        get() = LocalClickerColors.current
}

/** Wrap the whole app in this. The alchemy look stays the same whatever the system light/dark setting is. */
@Composable
fun ClickerAdventureTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalClickerColors provides AlchemyColors) {
        MaterialTheme(
            colorScheme = AlchemyColorScheme,
            typography = ClickerTypography,
            content = content
        )
    }
}

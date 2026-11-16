package com.example.idlealchemist.ui.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.example.idlealchemist.navigation.Route
import com.example.idlealchemist.navigation.TopLevelDestination
import com.example.idlealchemist.theme.ClickerTheme

/** Width at which the layout switches from a bottom bar (phone) to a side rail (desktop / tablet). */
private val WideLayoutBreakpoint = 600.dp

/**
 * Layout shared by every screen: alchemy gradient background plus the navigation bar.
 * Stateless: it is told which route is selected and reports taps through [onSelect].
 */
@Composable
fun AppScaffold(
    destinations: List<TopLevelDestination>,
    selected: NavKey?,
    onSelect: (Route) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val c = ClickerTheme.colors
    val ink = c.onGold
    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = ink,
        selectedTextColor = ClickerTheme.colors.vellum,
        indicatorColor = c.gold,
        unselectedIconColor = ClickerTheme.colors.vellum.copy(alpha = 0.85f),
        unselectedTextColor = ClickerTheme.colors.vellum.copy(alpha = 0.85f)
    )
    val railColors = NavigationRailItemDefaults.colors(
        selectedIconColor = ink,
        selectedTextColor = ClickerTheme.colors.vellum,
        indicatorColor = c.gold,
        unselectedIconColor = ClickerTheme.colors.vellum.copy(alpha = 0.85f),
        unselectedTextColor = ClickerTheme.colors.vellum.copy(alpha = 0.85f)
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(c.backgroundTop, c.backgroundBottom)))
    ) {
        val wide = maxWidth >= WideLayoutBreakpoint

        if (wide) {
            Row(Modifier.fillMaxSize()) {
                NavigationRail(containerColor = c.panel, contentColor = ClickerTheme.colors.vellum) {
                    destinations.forEach { d ->
                        NavigationRailItem(
                            selected = selected != null && selected::class == d.route::class,
                            onClick = { onSelect(d.route) },
                            icon = { Text(d.glyph, fontSize = 20.sp) },
                            label = { Text(d.label) },
                            colors = railColors
                        )
                    }
                }
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .windowInsetsPadding(WindowInsets.statusBars)
                ) { content() }
            }
        } else {
            Column(Modifier.fillMaxSize()) {
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                ) { content() }
                NavigationBar(containerColor = c.panel, contentColor = ClickerTheme.colors.vellum) {
                    destinations.forEach { d ->
                        NavigationBarItem(
                            selected = selected != null && selected::class == d.route::class,
                            onClick = { onSelect(d.route) },
                            icon = { Text(d.glyph, fontSize = 20.sp) },
                            label = { Text(d.label) },
                            colors = itemColors
                        )
                    }
                }
            }
        }
    }
}

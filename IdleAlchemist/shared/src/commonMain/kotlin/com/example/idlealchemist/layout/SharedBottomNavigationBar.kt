package com.example.idlealchemist.layout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.navigation.AppRoute

/**
 * Displays navigation for the application's top-level screens.
 *
 * Seven destinations do not fit with every label on a phone, so only the
 * selected destination shows its label.
 *
 * @param currentRoute Destination currently displayed.
 * @param canOpenTrail Whether an existing profile can be shown on the trail,
 * achievements, and shop.
 * @param onCreate Called when registration is selected.
 * @param onTrail Called when the trail is selected.
 * @param onAchievements Called when achievements are selected.
 * @param onShop Called when the potion shop is selected.
 * @param onGrimoire Called when the grimoire is selected.
 * @param onContact Called when the contact form is selected.
 * @param onCodex Called when the academy reference is selected.
 * @param modifier Configuration applied to the navigation bar.
 */
@Composable
fun SharedBottomNavigationBar(
    currentRoute: AppRoute?,
    canOpenTrail: Boolean,
    onCreate: () -> Unit,
    onTrail: () -> Unit,
    onAchievements: () -> Unit,
    onShop: () -> Unit,
    onGrimoire: () -> Unit,
    onContact: () -> Unit,
    onCodex: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier.border(1.dp, MaterialTheme.colorScheme.outline),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Destination(
            selected = currentRoute == AppRoute.Onboarding,
            glyph = "+",
            label = "Create",
            onClick = onCreate,
        )

        Destination(
            selected = currentRoute is AppRoute.CrucibleTrail ||
                    currentRoute is AppRoute.Stage,
            enabled = canOpenTrail,
            glyph = "◇",
            label = "Trail",
            onClick = onTrail,
        )

        Destination(
            selected = currentRoute is AppRoute.Achievements,
            enabled = canOpenTrail,
            glyph = "★",
            label = "Achievements",
            onClick = onAchievements,
        )

        Destination(
            selected = currentRoute is AppRoute.Shop,
            enabled = canOpenTrail,
            glyph = "$",
            label = "Shop",
            onClick = onShop,
        )

        Destination(
            selected = currentRoute == AppRoute.Grimoire,
            glyph = "▤",
            label = "Grimoire",
            onClick = onGrimoire,
        )

        Destination(
            selected = currentRoute == AppRoute.Contact ||
                    currentRoute == AppRoute.Messages ||
                    currentRoute is AppRoute.MessageDetail,
            glyph = "@",
            label = "Contact",
            onClick = onContact,
        )

        Destination(
            selected = currentRoute == AppRoute.AcademyCodex,
            glyph = "i",
            label = "Information",
            onClick = onCodex,
        )
    }
}

/**
 * One navigation bar destination with a text glyph as its icon.
 *
 * @param selected Whether this destination is currently displayed.
 * @param glyph Placeholder text icon, hidden from screen readers.
 * @param label Destination name, shown when selected.
 * @param onClick Called when the destination is selected.
 * @param enabled Whether the destination can be opened.
 */
@Composable
private fun RowScope.Destination(
    selected: Boolean,
    glyph: String,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    NavigationBarItem(
        selected = selected,
        enabled = enabled,
        onClick = onClick,
        alwaysShowLabel = false,
        icon = {
            Text(
                text = glyph,
                modifier = Modifier.clearAndSetSemantics {},
            )
        },
        label = {
            Text(
                text = label,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Visible,
            )
        },
    )
}

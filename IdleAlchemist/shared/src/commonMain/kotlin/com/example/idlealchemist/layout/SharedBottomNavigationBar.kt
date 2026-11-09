package com.example.idlealchemist.layout

import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.navigation.AppRoute

/**
 * Displays navigation for the application's four screens.
 *
 * @param currentRoute Destination currently displayed.
 * @param canOpenTrail Whether an existing profile can be shown on the trail.
 * @param onCreate Called when registration is selected.
 * @param onTrail Called when the trail is selected.
 * @param onGrimoire Called when the grimoire is selected.
 * @param onCodex Called when the academy reference is selected.
 * @param modifier Configuration applied to the navigation bar.
 */
@Composable
fun SharedBottomNavigationBar(
    currentRoute: AppRoute?,
    canOpenTrail: Boolean,
    onCreate: () -> Unit,
    onTrail: () -> Unit,
    onGrimoire: () -> Unit,
    onCodex: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier.border(1.dp, MaterialTheme.colorScheme.outline),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        NavigationBarItem(
            selected = currentRoute == AppRoute.Onboarding,
            onClick = onCreate,
            icon = {
                Text(
                    text = "+",
                    modifier = Modifier.clearAndSetSemantics {},
                )
            },
            label = {
                Text("Create")
            },
        )

        NavigationBarItem(
            selected = currentRoute is AppRoute.CrucibleTrail ||
                    currentRoute is AppRoute.Stage,
            enabled = canOpenTrail,
            onClick = onTrail,
            icon = {
                Text(
                    text = "◇",
                    modifier = Modifier.clearAndSetSemantics {},
                )
            },
            label = {
                Text("Trail")
            },
        )

        NavigationBarItem(
            selected = currentRoute == AppRoute.Grimoire,
            onClick = onGrimoire,
            icon = {
                Text(
                    text = "▤",
                    modifier = Modifier.clearAndSetSemantics {},
                )
            },
            label = {
                Text("Grimoire")
            },
        )

        NavigationBarItem(
            selected = currentRoute == AppRoute.AcademyCodex,
            onClick = onCodex,
            icon = {
                Text(
                    text = "i",
                    modifier = Modifier.clearAndSetSemantics {},
                )
            },
            label = {
                Text("Information")
            },
        )
    }
}
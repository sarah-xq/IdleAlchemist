package com.example.idlealchemist

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.idlealchemist.data.TrailProgressViewModel
import com.example.idlealchemist.designsystem.AlchemicalTheme
import com.example.idlealchemist.feature.stage.StageRoute
import com.example.idlealchemist.feature.codex.AcademyCodexScreen
import com.example.idlealchemist.feature.grimoire.GrimoireRoute
import com.example.idlealchemist.feature.trail.CrucibleTrailRoute
import com.example.idlealchemist.layout.SharedBottomNavigationBar
import com.example.idlealchemist.navigation.AppBackStackConfiguration
import com.example.idlealchemist.navigation.AppRoute
import com.example.idlealchemist.feature.onboarding.OnboardingRoute
import com.example.idlealchemist.ui.LocalProfileRepository
import com.example.idlealchemist.ui.ProfileRepositoryProvider
import kotlin.collections.get

/**
 * Assembles the four screens with shared navigation, theme, and session data.
 *
 * Registration remains the root destination. The shared repository stays
 * alive while navigation entries change.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    AlchemicalTheme {
        ProfileRepositoryProvider {
            val repository = LocalProfileRepository.current
            val profiles by repository.profiles.collectAsState()

            val backStack = rememberNavBackStack(
                AppBackStackConfiguration,
                AppRoute.Onboarding,
            )

            val trailProgress = viewModel { TrailProgressViewModel() }

            val currentRoute = backStack.lastOrNull() as? AppRoute

            val previousTrailProfile = backStack
                .filterIsInstance<AppRoute.CrucibleTrail>()
                .lastOrNull()
                ?.profile
                ?.takeIf { candidate ->
                    profiles.any { profile ->
                        profile.id == candidate.id
                    }
                }

            val trailProfile = previousTrailProfile
                ?: profiles.lastOrNull()

            val title = when (currentRoute) {
                AppRoute.Onboarding -> "Alchemical Workshop"
                is AppRoute.CrucibleTrail -> "Crucible Trail"
                is AppRoute.Stage -> "Stage ${currentRoute.stageNumber}"
                AppRoute.Grimoire -> "Grimoire"
                AppRoute.AcademyCodex -> "Academy Codex"
                null -> "Alchemical Workshop"
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(title)
                        },
                        navigationIcon = {
                            if (backStack.size > 1) {
                                TextButton(
                                    onClick = {
                                        popDestination(backStack)
                                    },
                                ) {
                                    Text("Back")
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor =
                                MaterialTheme.colorScheme.background,
                        ),
                    )
                },
                bottomBar = {
                    SharedBottomNavigationBar(
                        currentRoute = currentRoute,
                        canOpenTrail = trailProfile != null,
                        onCreate = {
                            navigateTo(
                                backStack = backStack,
                                destination = AppRoute.Onboarding,
                            )
                        },
                        onTrail = {
                            trailProfile?.let { profile ->
                                navigateTo(
                                    backStack = backStack,
                                    destination =
                                        AppRoute.CrucibleTrail(profile),
                                )
                            }
                        },
                        onGrimoire = {
                            navigateTo(
                                backStack = backStack,
                                destination = AppRoute.Grimoire,
                            )
                        },
                        onCodex = {
                            navigateTo(
                                backStack = backStack,
                                destination = AppRoute.AcademyCodex,
                            )
                        },
                    )
                },
            ) { innerPadding ->
                NavDisplay(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding),
                    backStack = backStack,
                    onBack = {
                        popDestination(backStack)
                    },
                    entryProvider = entryProvider {
                        entry<AppRoute.Onboarding> {
                            OnboardingRoute(
                                onProfileCreated = { profile ->
                                    navigateTo(
                                        backStack = backStack,
                                        destination =
                                            AppRoute.CrucibleTrail(profile),
                                    )
                                },
                            )
                        }

                        entry<AppRoute.CrucibleTrail> { route ->
                            CrucibleTrailRoute(
                                profile = route.profile,
                                trailProgress = trailProgress,
                                onPlayStage = { stageNumber ->
                                    navigateTo(
                                        backStack = backStack,
                                        destination =
                                            AppRoute.Stage(route.profile, stageNumber),
                                    )
                                },
                                onCreateAnother = {
                                    navigateTo(
                                        backStack = backStack,
                                        destination = AppRoute.Onboarding,
                                    )
                                },
                            )
                        }

                        entry<AppRoute.Stage> { route ->
                            StageRoute(
                                profile = route.profile,
                                stageNumber = route.stageNumber,
                                trailProgress = trailProgress,
                                onOpenStage = { stageNumber ->
                                    replaceDestination(
                                        backStack = backStack,
                                        destination =
                                            AppRoute.Stage(route.profile, stageNumber),
                                    )
                                },
                                onBackToTrail = {
                                    navigateTo(
                                        backStack = backStack,
                                        destination =
                                            AppRoute.CrucibleTrail(route.profile),
                                    )
                                },
                            )
                        }

                        entry<AppRoute.Grimoire> {
                            GrimoireRoute(
                                onCreateProfile = {
                                    navigateTo(
                                        backStack = backStack,
                                        destination = AppRoute.Onboarding,
                                    )
                                },
                                onProfileRemoved = { profileId ->
                                    trailProgress.removeProfile(profileId)
                                    removeProfileRoutes(
                                        backStack = backStack,
                                        profileId = profileId,
                                    )
                                },
                            )
                        }

                        entry<AppRoute.AcademyCodex> {
                            AcademyCodexScreen()
                        }
                    },
                )
            }
        }
    }
}

/**
 * Opens a destination without adding another copy of an existing entry.
 *
 * Existing destinations are revealed by removing entries above them.
 * New destinations are appended to the current history.
 *
 * @param backStack Application-owned navigation history.
 * @param destination Destination and any associated profile argument.
 */
private fun navigateTo(
    backStack: NavBackStack<NavKey>,
    destination: AppRoute,
) {
    if (backStack.lastOrNull() == destination) {
        return
    }

    val existingIndex = backStack.indexOfLast { route ->
        route == destination
    }

    if (existingIndex >= 0) {
        while (backStack.lastIndex > existingIndex) {
            backStack.removeAt(backStack.lastIndex)
        }
    } else {
        backStack.add(destination)
    }
}

/**
 * Swaps the current destination for another, so moving from one stage
 * to the next does not stack every stage in the history.
 *
 * @param backStack Application-owned navigation history.
 * @param destination Destination shown in place of the current one.
 */
private fun replaceDestination(
    backStack: NavBackStack<NavKey>,
    destination: AppRoute,
) {
    if (backStack.size > 1) {
        backStack[backStack.lastIndex] = destination
    } else {
        backStack.add(destination)
    }
}

/**
 * Removes the current destination while preserving the root entry.
 *
 * @param backStack Application-owned navigation history.
 */
private fun popDestination(
    backStack: NavBackStack<NavKey>,
) {
    if (backStack.size > 1) {
        backStack.removeAt(backStack.lastIndex)
    }
}

/**
 * Removes trail and stage snapshots belonging to a deleted profile.
 *
 * @param backStack Application-owned navigation history.
 * @param profileId Identifier removed from the shared repository.
 */
private fun removeProfileRoutes(
    backStack: NavBackStack<NavKey>,
    profileId: String,
) {
    for (index in backStack.lastIndex downTo 1) {
        val route = backStack[index]

        val routeProfileId = when (route) {
            is AppRoute.CrucibleTrail -> route.profile.id
            is AppRoute.Stage -> route.profile.id
            else -> null
        }

        if (routeProfileId == profileId) {
            backStack.removeAt(index)
        }
    }
}
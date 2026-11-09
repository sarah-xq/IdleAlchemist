package com.example.idlealchemist.feature.codex

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

/**
 * Describes one entry in an informational section.
 *
 * @property title Short heading identifying the topic.
 * @property description Explanation displayed beneath the heading.
 * @property formula Optional mathematical expression displayed separately.
 */
private data class CodexEntry(
    val title: String,
    val description: String,
    val formula: String? = null,
)

/**
 * Groups related academy reference entries.
 *
 * @property title Heading displayed on the section card.
 * @property introduction Optional explanation of the section's scope.
 * @property entries Ordered reference entries.
 */
private data class CodexSection(
    val title: String,
    val introduction: String? = null,
    val entries: List<CodexEntry>,
)

/**
 * Provides reference content for the current prototype.
 *
 * Proposed brewing calculations are explicitly separated from the
 * currently available profile and stage-inspection features.
 */
private val AcademySections = listOf(
    CodexSection(
        title = "Workshop Guide",
        entries = listOf(
            CodexEntry(
                title = "1. Create your alchemist",
                description = "Enter an alias, elemental affinity, " +
                        "motto or specialty, and an HTTPS avatar or sigil URL. " +
                        "Each registration creates a separate profile.",
            ),
            CodexEntry(
                title = "2. Explore the Crucible Trail",
                description = "Select a numbered stage to inspect its " +
                        "objective and reward, then play it. Each stage is " +
                        "its own clicker game: essence and upgrades start " +
                        "at zero, and completing the objective unlocks the " +
                        "next stage. Return to any unlocked stage to carry " +
                        "on where you left it.",
            ),
            CodexEntry(
                title = "3. Build your Grimoire",
                description = "View every alchemist created during the " +
                        "session. Filter by affinity or tier, change the sort " +
                        "order, and select a profile to inspect its details.",
            ),
            CodexEntry(
                title = "4. Manage your collection",
                description = "Creating another alchemist preserves " +
                        "existing profiles. Removing an alchemist requires " +
                        "confirmation and removes that profile from the " +
                        "session collection.",
            ),
        ),
    ),
    CodexSection(
        title = "Brewing Theory",
        introduction = "These calculations describe proposed gameplay. " +
                "Brewing, upgrades, and reward collection are not active yet. " +
                "Current trail objectives remain the values shown on each stage.",
        entries = listOf(
            CodexEntry(
                title = "Essence from tapping",
                formula = "E = N × P",
                description = "N is the number of taps. P is the essence " +
                        "produced per tap. For example, 20 taps at 1 essence " +
                        "per tap produce 20 essence.",
            ),
            CodexEntry(
                title = "Passive production",
                formula = "E = R × T",
                description = "R is the production rate in essence per " +
                        "second. T is elapsed time in seconds. A rate of " +
                        "2 essence per second produces 60 essence in " +
                        "30 seconds. New profiles currently begin at " +
                        "0 essence per second.",
            ),
            CodexEntry(
                title = "Upgrade cost scaling",
                formula = "C(k) = ceil(C₀ × Gᵏ)",
                description = "C₀ is the starting cost, G is the growth " +
                        "multiplier, and k is the number of upgrades already " +
                        "purchased. With C₀ = 10, G = 1.15, and k = 3, " +
                        "the next cost is 16 essence. The ceil operation " +
                        "rounds upward to a whole number.",
            ),
        ),
    ),
    CodexSection(
        title = "Multiplatform Specifications",
        entries = listOf(
            CodexEntry(
                title = "Shared application",
                description = "Kotlin Multiplatform shares models, " +
                        "validation, session data, and application logic. " +
                        "Compose Multiplatform renders the interface " +
                        "using Material 3.",
            ),
            CodexEntry(
                title = "Android",
                description = "The supplied configuration includes an " +
                        "Android shared-library target. An Android " +
                        "application hosts the shared interface. Remote " +
                        "avatars require the internet permission.",
            ),
            CodexEntry(
                title = "Desktop",
                description = "The supplied configuration includes a " +
                        "JVM target. The same screens adapt to desktop " +
                        "windows using constrained widths and scrolling.",
            ),
            CodexEntry(
                title = "Web",
                description = "Web is a planned platform. A browser " +
                        "target and web entry point still need to be added " +
                        "to the supplied project configuration.",
            ),
        ),
    ),
    CodexSection(
        title = "Credits & Tools",
        entries = listOf(
            CodexEntry(
                title = "Project",
                description = "Alchemical Clicker — an incremental " +
                        "brewing workshop concept.",
            ),
        ),
    ),
)

/**
 * Displays game guidance, reference calculations, and project information.
 *
 * The content uses the application's shared theme and scrolls on smaller
 * screens. It does not mutate profiles or application navigation.
 *
 * @param appVersion Display version supplied by the application. The default
 * identifies a development build without inventing a release number.
 * @param modifier Configuration applied to the screen container.
 */
@Composable
fun AcademyCodexScreen(
    appVersion: String = "Development build",
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colors.background,
                        colors.tertiary.copy(alpha = 0.08f),
                        colors.background,
                    ),
                ),
            ),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 960.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item(key = "academy-introduction") {
                AcademyIntroduction()
            }

            items(
                items = AcademySections,
                key = { section -> section.title },
            ) { section ->
                CodexSectionCard(section = section)
            }

            item(key = "build-information") {
                CodexSectionCard(
                    section = CodexSection(
                        title = "Version & Storage",
                        entries = listOf(
                            CodexEntry(
                                title = "Application version",
                                description = appVersion,
                            ),
                        ),
                    ),
                )
            }

            item(key = "academy-closing-note") {
                Text(
                    text = "Knowledge is the first ingredient " +
                            "of every great transmutation.",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.onSurfaceVariant,
                )
            }
        }
    }
}

/**
 * Displays the academy's introductory heading.
 */
@Composable
private fun AcademyIntroduction() {
    val colors = MaterialTheme.colorScheme

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Surface(
            color = colors.secondaryContainer,
            contentColor = colors.onSecondaryContainer,
            shape = MaterialTheme.shapes.small,
        ) {
            Text(
                text = "ACADEMY REFERENCE",
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 10.dp,
                ),
                style = MaterialTheme.typography.labelSmall,
            )
        }

        Text(
            text = "The Academy Codex",
            style = MaterialTheme.typography.headlineLarge,
            color = colors.onBackground,
        )

        Text(
            text = "Study the workshop, understand the brewing theory, " +
                    "and discover the tools behind your alchemical journey.",
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onSurfaceVariant,
        )
    }
}

/**
 * Displays a section of related academy reference entries.
 *
 * @param section Content and optional introductory explanation.
 * @param modifier Configuration applied to the section card.
 */
@Composable
private fun CodexSectionCard(
    section: CodexSection,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, colors.outline, MaterialTheme.shapes.large),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = section.title,
                style = MaterialTheme.typography.headlineSmall,
                color = colors.primary,
            )

            section.introduction?.let { introduction ->
                Text(
                    text = introduction,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                )
            }

            section.entries.forEachIndexed { index, entry ->
                if (index > 0) {
                    HorizontalDivider(
                        color = colors.outline.copy(alpha = 0.25f),
                    )
                }

                CodexEntryContent(entry = entry)
            }
        }
    }
}

/**
 * Displays an explanation with an optional mathematical expression.
 *
 * @param entry Reference content to render.
 */
@Composable
private fun CodexEntryContent(
    entry: CodexEntry,
) {
    val colors = MaterialTheme.colorScheme

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = entry.title,
            style = MaterialTheme.typography.titleMedium,
            color = colors.onSurface,
        )

        entry.formula?.let { formula ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colors.secondaryContainer,
                contentColor = colors.onSecondaryContainer,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = formula,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Monospace,
                )
            }
        }

        Text(
            text = entry.description,
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onSurfaceVariant,
        )
    }
}
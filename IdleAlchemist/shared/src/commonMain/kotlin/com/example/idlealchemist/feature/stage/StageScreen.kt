package com.example.idlealchemist.feature.stage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.model.LevelProgress
import com.example.idlealchemist.model.StageGoal
import com.example.idlealchemist.model.TrailStage
import com.example.idlealchemist.model.goalProgress
import com.example.idlealchemist.ui.screens.ClickerPage

/**
 * Plays one trail stage: the stage objective above that stage's own clicker game.
 *
 * @param stage Stage being played.
 * @param progress Saved progress for this stage.
 * @param nextStage Stage unlocked by completing this one, or null on the last stage.
 * @param onTap Called when the crucible is tapped.
 * @param onBuyExtraClick Called when the extra click upgrade is bought.
 * @param onBuyApprentice Called when an apprentice is hired.
 * @param onOpenNextStage Called to continue to [nextStage].
 * @param onBackToTrail Called to return to the stage map.
 * @param modifier Configuration applied to the screen container.
 */
@Composable
fun StageScreen(
    stage: TrailStage,
    progress: LevelProgress,
    nextStage: TrailStage?,
    onTap: () -> Unit,
    onBuyExtraClick: () -> Unit,
    onBuyApprentice: () -> Unit,
    onBuyExtraHand: () -> Unit,
    onOpenNextStage: () -> Unit,
    onBackToTrail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StageObjectiveCard(
            stage = stage,
            progress = progress,
            nextStage = nextStage,
            onOpenNextStage = onOpenNextStage,
            onBackToTrail = onBackToTrail,
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .padding(top = 10.dp),
        )

        ClickerPage(
            state = progress.game,
            onTap = onTap,
            onBuyExtraClick = onBuyExtraClick,
            onBuyApprentice = onBuyApprentice,
            onBuyExtraHand = onBuyExtraHand,
            modifier = Modifier.weight(1f),
        )
    }
}

/**
 * Shows the stage objective, how close the player is, and what to do
 * once the stage is complete.
 */
@Composable
private fun StageObjectiveCard(
    stage: TrailStage,
    progress: LevelProgress,
    nextStage: TrailStage?,
    onOpenNextStage: () -> Unit,
    onBackToTrail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val reached = progress.goalProgress(stage.goal)

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (progress.completed) {
                colors.primaryContainer
            } else {
                colors.surface
            },
        ),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "STAGE ${stage.number} · ${stage.element} · ${stage.title}",
                style = MaterialTheme.typography.labelSmall,
                color = colors.secondary,
            )

            Text(
                text = stage.objective,
                style = MaterialTheme.typography.bodyMedium,
            )

            LinearProgressIndicator(
                progress = { reached.toFloat() / stage.goal.target },
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = goalProgressLabel(stage.goal, reached),
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant,
            )

            if (progress.completed) {
                Text(
                    text = if (nextStage != null) {
                        "Stage complete! Stage ${nextStage.number} is unlocked."
                    } else {
                        "Stage complete! You finished the Crucible Trail."
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = colors.primary,
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (nextStage != null) {
                        Button(onClick = onOpenNextStage) {
                            Text("Stage ${nextStage.number}")
                        }
                    }

                    OutlinedButton(onClick = onBackToTrail) {
                        Text("Back to trail")
                    }
                }
            }
        }
    }
}

/**
 * Describes goal progress in words, e.g. "7 / 10 taps".
 *
 * @param goal Requirement being tracked.
 * @param reached Progress already capped at the goal's target.
 */
internal fun goalProgressLabel(goal: StageGoal, reached: Int): String = when (goal) {
    is StageGoal.Taps -> "$reached / ${goal.target} taps"
    is StageGoal.Combo -> "Best combo: $reached / ${goal.target} taps within ${goal.windowSeconds}s"
}

/**
 * Shown when a stage route cannot be played, for example a locked stage
 * restored from saved navigation.
 *
 * @param message Explanation shown to the player.
 * @param onBackToTrail Called to return to the stage map.
 */
@Composable
fun StageUnavailable(
    message: String,
    onBackToTrail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
            )

            OutlinedButton(onClick = onBackToTrail) {
                Text("Back to trail")
            }
        }
    }
}

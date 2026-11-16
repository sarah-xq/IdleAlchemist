package com.example.idlealchemist.feature.stage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.idlealchemist.data.TrailProgressViewModel
import com.example.idlealchemist.feature.trail.CrucibleTrailStages
import com.example.idlealchemist.model.AlchemistProfile
import com.example.idlealchemist.model.LevelProgress
import com.example.idlealchemist.model.ProfileProgress
import com.example.idlealchemist.model.StageStatus
import com.example.idlealchemist.model.stageStatuses
import kotlinx.coroutines.delay

/**
 * Connects one stage's saved game to the stage screen.
 *
 * The stage's game ticks once per second only while this screen is
 * showing, so apprentices in other stages do not keep brewing.
 *
 * @param profile Alchemist playing the stage, passed through navigation.
 * @param stageNumber Stage passed through navigation.
 * @param trailProgress Session-wide progress for every alchemist and stage.
 * @param onOpenStage Called with a stage number to play another stage.
 * @param onBackToTrail Called to return to the stage map.
 * @param modifier Layout configuration supplied by the application.
 */
@Composable
fun StageRoute(
    profile: AlchemistProfile,
    stageNumber: Int,
    trailProgress: TrailProgressViewModel,
    onOpenStage: (Int) -> Unit,
    onBackToTrail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val allProgress by trailProgress.progress.collectAsState()
    val profileProgress = allProgress[profile.id] ?: ProfileProgress()
    val levels = profileProgress.levels
    val stageIndex = CrucibleTrailStages.indexOfFirst { it.number == stageNumber }
    val stage = CrucibleTrailStages.getOrNull(stageIndex)
    val status = stageStatuses(CrucibleTrailStages, levels)[stageNumber]

    if (stage == null || status == null) {
        StageUnavailable("This stage does not exist.", onBackToTrail, modifier)
        return
    }

    if (status == StageStatus.LOCKED) {
        StageUnavailable(
            message = "Complete stage ${CrucibleTrailStages[stageIndex - 1].number} to unlock this stage.",
            onBackToTrail = onBackToTrail,
            modifier = modifier,
        )
        return
    }

    LaunchedEffect(profile.id, stageNumber) {
        while (true) {
            delay(1000L)
            trailProgress.tick(profile.id, stageNumber)
        }
    }

    val nextStage = CrucibleTrailStages.getOrNull(stageIndex + 1)
    val progress = levels[stageNumber] ?: LevelProgress()

    StageScreen(
        stage = stage,
        // Achievements are earned across all stages, so their message comes from the profile.
        progress = progress.copy(
            game = progress.game.copy(unlockNotice = profileProgress.achievements.notice),
        ),
        nextStage = nextStage,
        onTap = { trailProgress.tap(profile.id, stageNumber) },
        onBuyExtraClick = { trailProgress.buyExtraClick(profile.id, stageNumber) },
        onBuyApprentice = { trailProgress.buyApprentice(profile.id, stageNumber) },
        onBuyExtraHand = { trailProgress.buyExtraHand(profile.id, stageNumber) },
        onOpenNextStage = { nextStage?.let { onOpenStage(it.number) } },
        onBackToTrail = onBackToTrail,
        modifier = modifier,
    )
}

package com.example.idlealchemist.feature.trail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.idlealchemist.data.TrailProgressViewModel
import com.example.idlealchemist.model.AlchemistProfile
import com.example.idlealchemist.model.stageStatuses

/**
 * Owns the selected challenge for the Crucible Trail screen.
 *
 * The profile comes directly from the navigation argument. Only the
 * selected stage number is saved; stage definitions remain in the catalog
 * and stage progress lives in [trailProgress].
 *
 * @param profile Player snapshot passed from the onboarding route.
 * @param trailProgress Session-wide progress for every alchemist and stage.
 * @param onPlayStage Called with a stage number when an unlocked stage is played.
 * @param onCreateAnother Called when the player returns to registration.
 * @param modifier Layout configuration supplied by the application.
 */
@Composable
fun CrucibleTrailRoute(
    profile: AlchemistProfile,
    trailProgress: TrailProgressViewModel,
    onPlayStage: (Int) -> Unit,
    onCreateAnother: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedStageNumber by rememberSaveable(profile.id) {
        mutableStateOf<Int?>(null)
    }

    val allProgress by trailProgress.progress.collectAsState()
    val levels = allProgress[profile.id].orEmpty()

    val selectedStage = CrucibleTrailStages.firstOrNull { stage ->
        stage.number == selectedStageNumber
    }

    CrucibleTrailScreen(
        profile = profile,
        stages = CrucibleTrailStages,
        levels = levels,
        statuses = stageStatuses(CrucibleTrailStages, levels),
        selectedStage = selectedStage,
        onStageSelected = { stage ->
            selectedStageNumber = stage.number
        },
        onDismissStage = {
            selectedStageNumber = null
        },
        onPlayStage = { stage ->
            selectedStageNumber = null
            onPlayStage(stage.number)
        },
        onCreateAnother = onCreateAnother,
        modifier = modifier,
    )
}

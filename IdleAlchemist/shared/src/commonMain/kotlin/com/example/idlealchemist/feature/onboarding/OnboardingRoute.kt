package com.example.idlealchemist.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.idlealchemist.model.AlchemistProfile
import com.example.idlealchemist.model.ProfileDraft
import com.example.idlealchemist.model.validateProfileDraft
import com.example.idlealchemist.ui.LocalProfileRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Converts the editable form into a platform-saveable string. */
private val ProfileDraftSaver = Saver<ProfileDraft, String>(
    save = { draft ->
        Json.encodeToString(draft)
    },
    restore = { savedDraft ->
        Json.decodeFromString<ProfileDraft>(savedDraft)
    },
)

/**
 * Connects the onboarding form to shared data and navigation.
 *
 * Registration is synchronous because the session repository performs
 * no network or disk operations. The form resets after successful storage,
 * allowing another profile to be entered after returning to this route.
 *
 * @param onProfileCreated Called after storage with the newly created profile.
 * @param modifier Layout configuration supplied by the application.
 */
@Composable
fun OnboardingRoute(
    onProfileCreated: (AlchemistProfile) -> Unit,
    modifier: Modifier = Modifier,
    name: String = "",
    location: String = "",
    sendResult: Boolean = false,
) {
    val repository = LocalProfileRepository.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var draft by rememberSaveable(
        stateSaver = ProfileDraftSaver,
    ) {
        mutableStateOf(ProfileDraft())
    }

    var isSubmitting by remember {
        mutableStateOf(false)
    }

    val validation = validateProfileDraft(draft)

    OnboardingScreen(
        draft = draft,
        validation = validation,
        isSubmitting = isSubmitting,
        onDraftChange = { updatedDraft ->
            if (!isSubmitting) {
                draft = updatedDraft
            }
        },
        onSubmit = {
            // Revalidate the current state rather than trusting a prior render.
            if (
                !isSubmitting &&
                validateProfileDraft(draft).isValid
            ) {
                isSubmitting = true

                try {
                    val profile = repository.createProfile(draft)

                    draft = ProfileDraft()
                    keyboardController?.hide()
                    onProfileCreated(profile)
                } finally {
                    isSubmitting = false
                }
            }
        },
        modifier = modifier,
        sendResult = sendResult,
    )
}
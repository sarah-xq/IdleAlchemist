package com.example.idlealchemist.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.example.idlealchemist.rememberResultResponder
import androidx.compose.ui.unit.dp
import com.example.idlealchemist.model.ProfileDraft
import com.example.idlealchemist.model.ProfileValidation

/**
 * Displays the apprentice registration form.
 *
 * The constrained, scrollable layout supports phone portrait, landscape,
 * and desktop windows. All application state belongs to the caller.
 *
 * @param draft Current editable values.
 * @param validation Current field validation results.
 * @param isSubmitting Whether registration is already being handled.
 * @param onDraftChange Called with a replacement draft after an edit.
 * @param onSubmit Called when the player submits a valid form.
 * @param modifier Configuration applied to the screen container.
 */
@Composable
fun OnboardingScreen(
    draft: ProfileDraft,
    validation: ProfileValidation,
    isSubmitting: Boolean,
    onDraftChange: (ProfileDraft) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    sendResult: Boolean,
) {
    val sendResultAndClose = rememberResultResponder()
    val colors = MaterialTheme.colorScheme
    val canSubmit = validation.isValid && !isSubmitting

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colors.background,
                        colors.primaryContainer.copy(alpha = 0.25f),
                        colors.background,
                    ),
                ),
            )
            .imePadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 640.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Surface(
                color = colors.secondaryContainer,
                contentColor = colors.onSecondaryContainer,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = "APPRENTICE REGISTRATION",
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 10.dp,
                    ),
                    style = MaterialTheme.typography.labelSmall,
                )

            }

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = "Your alchemical\njourney begins.",
                    style = MaterialTheme.typography.headlineLarge,
                    color = colors.onBackground,
                )

                Text(
                    text = "Choose the identity you will carry into the " +
                            "workshop. Brew essences, follow the Crucible Trail, " +
                            "and build your grimoire.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.onSurfaceVariant,
                )
                if (sendResult) {
                    Button(onClick = {
                        sendResultAndClose("Welcome wizard!")
                    }) {
                        Text("Confirm & Return Result")
                    }
                }

            }

            Card(
                modifier = Modifier
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
                        text = "Create your alchemist",
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    Text(
                        text = "All four fields are required.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant,
                    )

                    ProfileTextField(
                        value = draft.alias,
                        onValueChange = { value ->
                            onDraftChange(draft.copy(alias = value))
                        },
                        label = "Alchemist alias",
                        hint = "The name displayed on your player card.",
                        error = validation.aliasError,
                        enabled = !isSubmitting,
                    )

                    ProfileTextField(
                        value = draft.elementalAffinity,
                        onValueChange = { value ->
                            onDraftChange(
                                draft.copy(elementalAffinity = value),
                            )
                        },
                        label = "Elemental affinity",
                        hint = "For example: Fire, Water, Earth, Air, or Aether.",
                        error = validation.affinityError,
                        enabled = !isSubmitting,
                    )

                    ProfileTextField(
                        value = draft.motto,
                        onValueChange = { value ->
                            onDraftChange(draft.copy(motto = value))
                        },
                        label = "Motto or specialty",
                        hint = "For example: From embers, discovery.",
                        error = validation.mottoError,
                        enabled = !isSubmitting,
                    )

                    ProfileTextField(
                        value = draft.avatarUrl,
                        onValueChange = { value ->
                            onDraftChange(draft.copy(avatarUrl = value))
                        },
                        label = "Avatar or sigil image URL",
                        hint = "Paste a direct HTTPS link to your image.",
                        error = validation.avatarUrlError,
                        enabled = !isSubmitting,
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Done,
                        onDone = {
                            if (canSubmit) {
                                onSubmit()
                            }
                        },
                    )

                    Button(
                        onClick = onSubmit,
                        enabled = canSubmit,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        contentPadding = PaddingValues(
                            horizontal = 20.dp,
                            vertical = 16.dp,
                        ),
                    ) {
                        Text(
                            text = if (isSubmitting) {
                                "Registering..."
                            } else {
                                "Enter the workshop"
                            },
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }

                    Text(
                        text = if (validation.isValid) {
                            "Your apprentice profile is ready."
                        } else {
                            "Complete the required fields to begin."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (validation.isValid) {
                            colors.secondary
                        } else {
                            colors.onSurfaceVariant
                        },
                    )
                }
            }

            Text(
                text = "Every master alchemist began with a single brew.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
            )
        }
    }
}

/**
 * Displays one labeled input with supporting guidance or an inline error.
 *
 * Empty fields initially show guidance. Invalid nonempty values display
 * their error immediately, including values containing only whitespace.
 *
 * @param value Current editable text.
 * @param onValueChange Called after the player edits the text.
 * @param label Persistent field label.
 * @param hint Guidance displayed when no error is visible.
 * @param error Validation message, or null when valid.
 * @param enabled Whether the input accepts edits.
 * @param keyboardType Keyboard suited to this input.
 * @param imeAction Action displayed by a software keyboard.
 * @param onDone Called when the keyboard's Done action is selected.
 * @param modifier Layout configuration for the text field.
 */
@Composable
private fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    hint: String,
    error: String?,
    enabled: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onDone: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val showError = value.isNotEmpty() && error != null

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        label = {
            Text(text = label)
        },
        supportingText = {
            Text(
                text = if (showError) {
                    error.orEmpty()
                } else {
                    hint
                },
            )
        },
        isError = showError,
        shape = MaterialTheme.shapes.small,
        keyboardOptions = KeyboardOptions(
            capitalization = if (keyboardType == KeyboardType.Uri) {
                KeyboardCapitalization.None
            } else {
                KeyboardCapitalization.Sentences
            },
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            },
            onDone = {
                onDone()
            },
        ),
    )
}
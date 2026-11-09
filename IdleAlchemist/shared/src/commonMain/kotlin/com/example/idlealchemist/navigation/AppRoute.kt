package com.example.idlealchemist.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.idlealchemist.model.AlchemistProfile
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Defines the application's destinations: four tabs plus the stage being played.
 */
@Serializable
sealed class AppRoute : NavKey {

    /** Opens apprentice registration. */
    @Serializable
    data object Onboarding : AppRoute()

    /**
     * Opens the trail with a profile passed through navigation.
     *
     * @property profile Snapshot of the selected alchemist.
     */
    @Serializable
    data class CrucibleTrail(
        val profile: AlchemistProfile,
    ) : AppRoute()

    /**
     * Plays one trail stage as its own clicker game.
     *
     * @property profile Snapshot of the alchemist playing the stage.
     * @property stageNumber Number of the stage being played.
     */
    @Serializable
    data class Stage(
        val profile: AlchemistProfile,
        val stageNumber: Int,
    ) : AppRoute()

    /** Opens the cumulative collection from the shared repository. */
    @Serializable
    data object Grimoire : AppRoute()

    /** Opens game guidance, formulas, and application information. */
    @Serializable
    data object AcademyCodex : AppRoute()
}

/**
 * Registers all destination serializers for multiplatform saved state.
 */
val AppBackStackConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(
                AppRoute.Onboarding::class,
                AppRoute.Onboarding.serializer(),
            )

            subclass(
                AppRoute.CrucibleTrail::class,
                AppRoute.CrucibleTrail.serializer(),
            )

            subclass(
                AppRoute.Stage::class,
                AppRoute.Stage.serializer(),
            )

            subclass(
                AppRoute.Grimoire::class,
                AppRoute.Grimoire.serializer(),
            )

            subclass(
                AppRoute.AcademyCodex::class,
                AppRoute.AcademyCodex.serializer(),
            )
        }
    }
}
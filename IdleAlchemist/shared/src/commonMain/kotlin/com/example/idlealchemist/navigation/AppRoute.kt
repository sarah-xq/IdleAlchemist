package com.example.idlealchemist.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.idlealchemist.model.AlchemistProfile
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Defines the application's destinations: the navigation bar tabs, the
 * stage being played, and the sent-message screens under Contact.
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

    /**
     * Shows the achievements an alchemist has earned across every stage.
     *
     * @property profile Snapshot of the alchemist whose achievements are shown.
     */
    @Serializable
    data class Achievements(
        val profile: AlchemistProfile,
    ) : AppRoute()

    /**
     * Sells potions that boost the alchemist's last played stage.
     *
     * @property profile Snapshot of the alchemist buying potions.
     */
    @Serializable
    data class Shop(
        val profile: AlchemistProfile,
    ) : AppRoute()

    /** Opens the cumulative collection from the shared repository. */
    @Serializable
    data object Grimoire : AppRoute()

    /** Opens the contact form. */
    @Serializable
    data object Contact : AppRoute()

    /** Lists every message sent from the contact form. */
    @Serializable
    data object Messages : AppRoute()

    /**
     * Shows one sent message.
     *
     * @property id Identifier of the message, passed as the navigation parameter.
     */
    @Serializable
    data class MessageDetail(
        val id: Int,
    ) : AppRoute()

    /** Opens game guidance, formulas, and application information. */
    @Serializable
    data object AcademyCodex : AppRoute()
}

/** Alchemist a destination belongs to, or null for destinations shared by everyone. */
val AppRoute.profileOrNull: AlchemistProfile?
    get() = when (this) {
        is AppRoute.CrucibleTrail -> profile
        is AppRoute.Stage -> profile
        is AppRoute.Achievements -> profile
        is AppRoute.Shop -> profile
        else -> null
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
                AppRoute.Achievements::class,
                AppRoute.Achievements.serializer(),
            )

            subclass(
                AppRoute.Shop::class,
                AppRoute.Shop.serializer(),
            )

            subclass(
                AppRoute.Grimoire::class,
                AppRoute.Grimoire.serializer(),
            )

            subclass(
                AppRoute.Contact::class,
                AppRoute.Contact.serializer(),
            )

            subclass(
                AppRoute.Messages::class,
                AppRoute.Messages.serializer(),
            )

            subclass(
                AppRoute.MessageDetail::class,
                AppRoute.MessageDetail.serializer(),
            )

            subclass(
                AppRoute.AcademyCodex::class,
                AppRoute.AcademyCodex.serializer(),
            )
        }
    }
}

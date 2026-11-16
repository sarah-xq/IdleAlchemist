package com.example.idlealchemist.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/** Every screen in the app. A sealed class, so a `when` over routes must cover all of them. */
@Serializable
sealed class Route : NavKey {
    @Serializable
    data object Clicker : Route()

    @Serializable
    data object Achievements : Route()

    @Serializable
    data object Shop : Route()

    @Serializable
    data object Contact : Route()

    /** List of every message sent from the Contact screen. */
    @Serializable
    data object Messages : Route()

    /** One message, identified by the id passed as the navigation parameter. */
    @Serializable
    data class MessageDetail(val id: Int) : Route()
}

/**
 * Non-Android targets (Desktop) have no reflection, so every route must be registered here
 * for the back stack to be saved. Add new routes to this list as well.
 */
val NavConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Route.Clicker::class, Route.Clicker.serializer())
            subclass(Route.Achievements::class, Route.Achievements.serializer())
            subclass(Route.Shop::class, Route.Shop.serializer())
            subclass(Route.Contact::class, Route.Contact.serializer())
            subclass(Route.Messages::class, Route.Messages.serializer())
            subclass(Route.MessageDetail::class, Route.MessageDetail.serializer())
        }
    }
}

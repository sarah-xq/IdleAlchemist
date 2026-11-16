package com.example.idlealchemist.model

/** One message the player "sent" from the Contact form. Immutable. */
data class ContactMessage(
    val id: Int,
    val name: String,
    val email: String,
    val subject: String,
    val message: String,
    val screenshotLink: String
)

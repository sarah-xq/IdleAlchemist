package com.example.idlealchemist.data

import androidx.lifecycle.ViewModel
import com.example.idlealchemist.model.ContactMessage
import com.example.idlealchemist.model.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Keeps the game and the sent messages alive across rotation. One instance is shared by every screen. */
class GameViewModel : ViewModel() {
    private val repository = GameRepository()

    val state: StateFlow<GameState> = repository.state

    private val _messages = MutableStateFlow<List<ContactMessage>>(emptyList())
    /** Every message sent from the Contact form, oldest first. */
    val messages: StateFlow<List<ContactMessage>> = _messages

    private var nextMessageId = 1

    fun tap() = repository.tap()
    fun buyExtraClick() = repository.buyExtraClick()
    fun buyApprentice() = repository.buyApprentice()

    fun buyExtraHand() = repository.buyExtraHand()
    fun buyPotion(id: String) = repository.buyPotion(id)
    fun tick() = repository.tick()
    fun deleteAchievement(id: String) = repository.deleteAchievement(id)

    /** Mock send: stores the message and returns its id so the screen can open it. */
    fun sendMessage(name: String, email: String, subject: String, message: String, screenshotLink: String): Int {
        val id = nextMessageId++
        val sent = ContactMessage(id, name.trim(), email.trim(), subject.trim(), message.trim(), screenshotLink.trim())
        _messages.update { it + sent }
        return id
    }

    fun removeMessage(id: Int) = _messages.update { list -> list.filterNot { it.id == id } }
}

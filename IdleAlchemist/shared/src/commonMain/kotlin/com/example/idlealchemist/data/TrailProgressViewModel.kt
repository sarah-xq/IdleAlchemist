package com.example.idlealchemist.data

import androidx.lifecycle.ViewModel
import com.example.idlealchemist.model.ContactMessage
import com.example.idlealchemist.model.LevelMap
import com.example.idlealchemist.model.ProfileProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Keeps level map progress and sent messages alive across rotation. One instance is shared by the
 * trail/map, stage, achievements, shop and contact screens.
 */
class TrailProgressViewModel : ViewModel() {
    private val repository = TrailProgressRepository()

    /** Saved trail progress keyed by profile ID. */
    val progress: StateFlow<Map<String, ProfileProgress>> = repository.progress

    /** Currently active level map ID. */
    val currentMapId: StateFlow<Int> = repository.currentMapId

    /** Currently active level map. */
    val currentLevelMap: LevelMap get() = repository.currentLevelMap

    /** All available level maps. */
    val availableMaps: List<LevelMap> get() = repository.availableMaps

    /** Selects a level map by its internal ID. */
    fun selectMap(mapId: Int) = repository.selectMap(mapId)

    fun tap(profileId: String, stageNumber: Int) = repository.tap(profileId, stageNumber)
    fun buyExtraClick(profileId: String, stageNumber: Int) = repository.buyExtraClick(profileId, stageNumber)
    fun buyApprentice(profileId: String, stageNumber: Int) = repository.buyApprentice(profileId, stageNumber)
    fun buyPotion(profileId: String, potionId: String) = repository.buyPotion(profileId, potionId)
    fun tick(profileId: String, stageNumber: Int) = repository.tick(profileId, stageNumber)
    fun deleteAchievement(profileId: String, achievementId: String) =
        repository.deleteAchievement(profileId, achievementId)
    fun shopStage(profileId: String): Int = repository.shopStage(profileId)
    fun removeProfile(profileId: String) = repository.removeProfile(profileId)

    private val _messages = MutableStateFlow<List<ContactMessage>>(emptyList())
    /** Every message sent from the Contact form, oldest first. */
    val messages: StateFlow<List<ContactMessage>> = _messages

    private var nextMessageId = 1

    /** Mock send: stores the message and returns its id so the screen can open it. */
    fun sendMessage(name: String, email: String, subject: String, message: String, screenshotLink: String): Int {
        val id = nextMessageId++
        val sent = ContactMessage(id, name.trim(), email.trim(), subject.trim(), message.trim(), screenshotLink.trim())
        _messages.update { it + sent }
        return id
    }

    fun removeMessage(id: Int) = _messages.update { list -> list.filterNot { it.id == id } }

    fun buyExtraHand(profileId: String, stageNumber: Int) = repository.buyExtraHand(profileId, stageNumber)
}

package com.example.idlealchemist.data

import com.example.idlealchemist.model.GameState
import com.example.idlealchemist.model.afterBuyApprentice
import com.example.idlealchemist.model.afterBuyExtraClick
import com.example.idlealchemist.model.afterBuyPotion
import com.example.idlealchemist.model.afterDeleteAchievement
import com.example.idlealchemist.model.afterTap
import com.example.idlealchemist.model.afterTick
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * The single source of truth for the game, shared by the clicker, achievements and shop screens.
 * Screens never change the state themselves; they call these functions. The rules themselves
 * live in GameRules.kt.
 */
class GameRepository {
    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    fun tap() = _state.update { it.afterTap() }
    fun buyExtraClick() = _state.update { it.afterBuyExtraClick() }
    fun buyApprentice() = _state.update { it.afterBuyApprentice() }
    fun buyPotion(id: String) = _state.update { it.afterBuyPotion(id) }
    fun tick() = _state.update { it.afterTick() }
    fun deleteAchievement(id: String) = _state.update { it.afterDeleteAchievement(id) }
}

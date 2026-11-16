package com.example.idlealchemist.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.idlealchemist.data.GameViewModel
import com.example.idlealchemist.ui.screens.AchievementsScreen
import com.example.idlealchemist.ui.screens.ClickerPage
import com.example.idlealchemist.ui.screens.ContactScreen
import com.example.idlealchemist.ui.screens.MessageDetailScreen
import com.example.idlealchemist.ui.screens.MessagesScreen
import com.example.idlealchemist.ui.screens.ShopScreen
import com.example.idlealchemist.ui.shell.AppScaffold
import com.example.idlealchemist.navigation.navigateTopLevel
import kotlinx.coroutines.delay

/**
 * Stateful root of the app: owns the back stack and the shared game, and connects them to
 * the stateless scaffold and screens. Starts on the clicker.
 */
@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(NavConfiguration, Route.Clicker)
    val game = viewModel { GameViewModel() }
    val state by game.state.collectAsState()
    val messages by game.messages.collectAsState()

    // One game tick per second, whichever screen is showing (apprentices keep working
    // while the player browses achievements or the shop).
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            game.tick()
        }
    }

    // The message list and message detail belong to the Contact tab, so keep it highlighted.
    val current = backStack.lastOrNull()
    val selectedTab = when (current) {
        is Route.Messages, is Route.MessageDetail -> Route.Contact
        else -> current
    }

    AppScaffold(
        destinations = topLevelDestinations,
        selected = selectedTab,
        onSelect = { route -> backStack.navigateTopLevel(route) }
    ) {
        NavDisplay(
            backStack = backStack,
            entryProvider = entryProvider {
                entry<Route.Clicker> {
                    ClickerPage(
                        state = state,
                        onTap = game::tap,
                        onBuyExtraClick = game::buyExtraClick,
                        onBuyApprentice = game::buyApprentice,
                        onBuyExtraHand = game::buyExtraHand
                    )
                }
                entry<Route.Achievements> {
                    AchievementsScreen(state = state, onDelete = game::deleteAchievement)
                }
                entry<Route.Shop> {
                    ShopScreen(state = state, onBuy = game::buyPotion)
                }
                entry<Route.Contact> {
                    ContactScreen(
                        sentCount = messages.size,
                        onSend = game::sendMessage,
                        onPreview = { id -> backStack.add(Route.MessageDetail(id)) },
                        onOpenList = { backStack.add(Route.Messages) }
                    )
                }
                entry<Route.Messages> {
                    MessagesScreen(
                        messages = messages,
                        onOpen = { id -> backStack.add(Route.MessageDetail(id)) },
                        onRemove = game::removeMessage,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
                // The id travels inside the route: this is the navigation parameter.
                entry<Route.MessageDetail> { key ->
                    MessageDetailScreen(
                        message = messages.firstOrNull { it.id == key.id },
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }
        )
    }
}

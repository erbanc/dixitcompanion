package fr.erban.dxitcompanion.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.erban.dxitcompanion.db.game.GameViewModel
import fr.erban.dxitcompanion.db.player.PlayerConverter
import fr.erban.dxitcompanion.db.player.PlayerViewModel
import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.ui.CurrentGameViewModel
import fr.erban.dxitcompanion.ui.screens.EndTurnScreen
import fr.erban.dxitcompanion.ui.screens.EveryoneFoundScreen
import fr.erban.dxitcompanion.ui.screens.HomeScreen
import fr.erban.dxitcompanion.ui.screens.RulesScreen
import fr.erban.dxitcompanion.ui.screens.ScoresResultScreen
import fr.erban.dxitcompanion.ui.screens.SelectObjectivesScreen
import fr.erban.dxitcompanion.ui.screens.SelectPlayersScreen
import fr.erban.dxitcompanion.ui.screens.SelectStoryTellerScreen
import fr.erban.dxitcompanion.ui.screens.SelectVotesScreen
import fr.erban.dxitcompanion.ui.screens.StatsScreen
import fr.erban.dxitcompanion.ui.screens.WhoDidFindScreen

@Composable
fun DixitNavGraph(
    navController: NavHostController,
    playerViewModel: PlayerViewModel,
    gameViewModel: GameViewModel,
    gameState: CurrentGameViewModel = viewModel()
) {
    NavHost(navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                onNewGame = { navController.navigate(Screen.SelectPlayers.route) },
                onStats   = { navController.navigate(Screen.Stats.route) },
                onRules   = { navController.navigate(Screen.Rules.route) }
            )
        }

        composable(Screen.SelectPlayers.route) {
            val existingPlayers by playerViewModel.players.observeAsState(initial = emptyList())
            SelectPlayersScreen { names ->
                val players = names.map { name ->
                    val entity = existingPlayers.find { it.name == name }
                    if (entity != null) PlayerConverter.toBean(entity)
                    else PlayerBean(name = name)
                }
                gameState.startGame(players, Int.MAX_VALUE, Int.MAX_VALUE)
                navController.navigate(Screen.SelectObjectives.route)
            }
        }

        composable(Screen.SelectObjectives.route) {
            SelectObjectivesScreen { pts, turns ->
                gameState.startGame(gameState.game.players, pts, turns)
                navController.navigate(Screen.SelectStoryteller.route)
            }
        }

        composable(Screen.SelectStoryteller.route) {
            // Block back navigation during a game to avoid corrupting game state
            BackHandler(enabled = true) { /* consume the back event, do nothing */ }
            SelectStoryTellerScreen(
                players = gameState.game.players,
                turnNumber = gameState.game.currentTurn
            ) { storyteller ->
                gameState.setStoryteller(storyteller)
                navController.navigate(Screen.EveryoneFound.route)
            }
        }

        composable(Screen.EveryoneFound.route) {
            BackHandler(enabled = true) { /* consume the back event, do nothing */ }
            EveryoneFoundScreen(
                turnNumber = gameState.game.currentTurn,
                storytellerName = gameState.turn.storyTeller?.name ?: ""
            ) { everybodyFound ->
                if (everybodyFound) {
                    gameState.setEveryoneFound(true)
                    navController.navigate(Screen.EndTurn.route)
                } else {
                    gameState.setEveryoneFound(false)
                    navController.navigate(Screen.WhoDidFind.route)
                }
            }
        }

        composable(Screen.WhoDidFind.route) {
            BackHandler(enabled = true) { /* consume the back event, do nothing */ }
            val nonStorytellers = gameState.game.players.filter { it.name != gameState.turn.storyTeller?.name }
            WhoDidFindScreen(
                players = nonStorytellers,
                turnNumber = gameState.game.currentTurn
            ) { found ->
                gameState.setWhoFound(found)
                navController.navigate(Screen.SelectVotes.route)
            }
        }

        composable(Screen.SelectVotes.route) {
            BackHandler(enabled = true) { /* consume the back event, do nothing */ }
            val storyteller = gameState.turn.storyTeller!!
            val nonStorytellers = gameState.game.players.filter { it.name != storyteller.name }
            SelectVotesScreen(
                voters = nonStorytellers,
                storyteller = storyteller,
                candidates = nonStorytellers,
                turnNumber = gameState.game.currentTurn
            ) { votes ->
                gameState.setVotes(votes)
                navController.navigate(Screen.EndTurn.route)
            }
        }

        composable(Screen.EndTurn.route) {
            BackHandler(enabled = true) { /* consume the back event, do nothing */ }
            val currentTurn = gameState.game.currentTurn
            val (updatedGame, endGame) = remember(currentTurn) { gameState.computeEndTurn() }
            EndTurnScreen(
                game = updatedGame,
                turnNumber = updatedGame.currentTurn,
                endGame = endGame,
                winnerName = updatedGame.nameWinner
            ) {
                if (endGame) {
                    gameViewModel.insert(updatedGame)
                    playerViewModel.update(PlayerConverter.toEntities(updatedGame.players))
                    navController.navigate(Screen.ScoresResult.route) {
                        popUpTo(Screen.Home.route)
                    }
                } else {
                    gameState.advanceTurn()
                    navController.navigate(Screen.SelectStoryteller.route) {
                        popUpTo(Screen.SelectStoryteller.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Screen.ScoresResult.route) {
            ScoresResultScreen(game = gameState.game) {
                gameState.resetGame()
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            }
        }

        composable(Screen.Stats.route) {
            StatsScreen(playerViewModel)
        }

        composable(Screen.Rules.route) {
            RulesScreen()
        }
    }
}

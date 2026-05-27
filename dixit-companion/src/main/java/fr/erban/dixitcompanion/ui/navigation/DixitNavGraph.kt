package fr.erban.dixitcompanion.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.erban.dixitcompanion.db.game.GameViewModel
import fr.erban.dixitcompanion.db.player.PlayerConverter
import fr.erban.dixitcompanion.db.player.PlayerViewModel
import fr.erban.dixitcompanion.game.player.PlayerBean
import fr.erban.dixitcompanion.ui.CurrentGameViewModel
import fr.erban.dixitcompanion.ui.screens.EndTurnScreen
import fr.erban.dixitcompanion.ui.screens.EveryoneFoundScreen
import fr.erban.dixitcompanion.ui.screens.HomeScreen
import fr.erban.dixitcompanion.ui.screens.PlayerDraft
import fr.erban.dixitcompanion.ui.screens.RulesScreen
import fr.erban.dixitcompanion.ui.screens.ScoresResultScreen
import fr.erban.dixitcompanion.ui.screens.SelectObjectivesScreen
import fr.erban.dixitcompanion.ui.screens.SelectPlayersScreen
import fr.erban.dixitcompanion.ui.screens.SelectStoryTellerScreen
import fr.erban.dixitcompanion.ui.screens.SelectVotesScreen
import fr.erban.dixitcompanion.ui.screens.StatsScreen
import fr.erban.dixitcompanion.ui.screens.WhoDidFindScreen

@Composable
fun DixitNavGraph(
    navController: NavHostController,
    playerViewModel: PlayerViewModel,
    gameViewModel: GameViewModel,
    gameState: CurrentGameViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                onNewGame = { navController.navigate(Screen.SelectPlayers.route) },
                onStats   = { navController.navigate(Screen.Stats.route) },
                onRules   = { navController.navigate(Screen.Rules.route) }
            )
        }

        composable(Screen.SelectPlayers.route) {
            val existingPlayers by playerViewModel.players.observeAsState(initial = emptyList())
            val rematchSeed = gameState.rematchTemplate
            val initial = remember(rematchSeed) {
                rematchSeed?.map { PlayerDraft(it.name, it.emoji, it.colorHex) } ?: emptyList()
            }
            // Consume the template once shown
            remember(rematchSeed) {
                gameState.consumeRematchTemplate()
                Unit
            }
            SelectPlayersScreen(
                initialPlayers = initial,
                knownPlayers = existingPlayers.map { PlayerDraft(it.name, it.emoji, it.colorHex) }
            ) { drafts ->
                val players = drafts.map { draft ->
                    val entity = existingPlayers.find { it.name.equals(draft.name, true) }
                    if (entity != null) PlayerConverter.toBean(entity).copy(
                        emoji = draft.emoji,
                        colorHex = draft.colorHex
                    )
                    else PlayerBean(
                        name = draft.name,
                        emoji = draft.emoji,
                        colorHex = draft.colorHex
                    )
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
                storyteller = gameState.turn.storyTeller
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
            val nonStorytellers = gameState.game.players
                .filter { it.name != gameState.turn.storyTeller?.name }
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
            val nonStorytellers = gameState.game.players
                .filter { it.name != storyteller.name }
            val whoFound = gameState.turn.whoFound
            // Players who already found don't need to vote — their vote is implicit
            val voters = nonStorytellers.filter { p -> whoFound.none { it.name == p.name } }
            SelectVotesScreen(
                voters = voters,
                storyteller = storyteller,
                candidates = nonStorytellers,
                turnNumber = gameState.game.currentTurn
            ) { votes ->
                val autoVotes = whoFound.map { finder ->
                    fr.erban.dixitcompanion.game.turn.bean.VoteBean(voter = finder, elected = storyteller)
                }
                gameState.setVotes(votes + autoVotes)
                navController.navigate(Screen.EndTurn.route)
            }
        }

        composable(Screen.EndTurn.route) {
            BackHandler(enabled = true) { /* consume the back event, do nothing */ }
            val (updatedGame, endGame) = remember { gameState.computeEndTurn() }
            EndTurnScreen(
                game = updatedGame,
                turnNumber = updatedGame.currentTurn,
                endGame = endGame,
                winnerName = updatedGame.nameWinner
            ) {
                if (endGame) {
                    gameViewModel.insert(updatedGame)
                    playerViewModel.upsert(PlayerConverter.toEndGameEntities(updatedGame.players, updatedGame.nameWinner))
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
            ScoresResultScreen(
                game = gameState.game,
                onHome = {
                    gameState.resetGame()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onRematch = {
                    // Keep rematchTemplate so SelectPlayers can pre-fill the roster;
                    // startGame() inside SelectPlayers will overwrite the current game state.
                    navController.navigate(Screen.SelectPlayers.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(Screen.Stats.route) {
            StatsScreen(playerViewModel, gameViewModel)
        }

        composable(Screen.Rules.route) {
            RulesScreen()
        }
    }
}

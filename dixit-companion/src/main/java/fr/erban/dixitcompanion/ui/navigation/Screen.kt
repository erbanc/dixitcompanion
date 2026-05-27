package fr.erban.dixitcompanion.ui.navigation

sealed class Screen(val route: String) {
    object Home              : Screen("home")
    object SelectPlayers     : Screen("select_players")
    object SelectObjectives  : Screen("select_objectives")
    object SelectStoryteller : Screen("select_storyteller")
    object EveryoneFound     : Screen("everyone_found")
    object WhoDidFind        : Screen("who_did_find")
    object SelectVotes       : Screen("select_votes")
    object EndTurn           : Screen("end_turn")
    object ScoresResult      : Screen("scores_result")
    object Stats             : Screen("stats")
    object Rules             : Screen("rules")
}

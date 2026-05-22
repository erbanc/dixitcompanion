package fr.erban.dxitcompanion.game

import android.os.Parcelable
import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.game.player.TurnScore
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameBean(
    val players: List<PlayerBean>,
    val currentTurn: Int = 0,
    val pointsToWin: Int = 0,
    val maxTurns: Int = Int.MAX_VALUE,
    val finished: Boolean = false,
    val nameWinner: String? = null
) : Parcelable {

    fun getScoresheet(): Map<String, Map<String, Int>> =
        players.associate { player ->
            player.name to player.scoresheet.associate { it.turn.toString() to it.score }
        }
}

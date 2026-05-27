package fr.erban.dixitcompanion.game

import android.os.Parcelable
import fr.erban.dixitcompanion.game.player.PlayerBean
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameBean(
    val players: List<PlayerBean>,
    val currentTurn: Int = 0,
    val pointsToWin: Int = 0,
    val maxTurns: Int = Int.MAX_VALUE,
    val finished: Boolean = false,
    val nameWinner: String? = null,
    val startedAt: Long = 0L,
    val endedAt: Long = 0L
) : Parcelable {

    fun getScoresheet(): Map<String, Map<String, Int>> =
        players.associate { player ->
            player.name to player.scoresheet.associate { it.turn.toString() to it.score }
        }

    val durationMillis: Long get() = if (endedAt > startedAt) endedAt - startedAt else 0L
}

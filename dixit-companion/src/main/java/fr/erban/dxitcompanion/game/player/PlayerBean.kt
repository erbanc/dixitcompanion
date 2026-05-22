package fr.erban.dxitcompanion.game.player

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerBean(
    val name: String,
    val currentScore: Int = 0,
    val scoreLastTurn: Int? = null,
    val scoresheet: List<TurnScore> = emptyList(),
    val nbGames: Int = 0,
    val nbWins: Int = 0,
    val persisted: Boolean = false
) : Parcelable

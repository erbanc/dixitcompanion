package fr.erban.dixitcompanion.game.player

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
    val nbAsStoryteller: Int = 0,
    val nbAsVoter: Int = 0,
    val nbFoundStoryteller: Int = 0,
    val nbStorytellerOptimalTurns: Int = 0,
    val totalPoints: Int = 0,
    val persisted: Boolean = false,
    val colorHex: String = "#5C3A9E",
    val emoji: String = "🎭"
) : Parcelable

package fr.erban.dxitcompanion.game.player

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TurnScore(
    val turn: Int,
    val score: Int
) : Parcelable

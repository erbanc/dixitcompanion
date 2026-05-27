package fr.erban.dixitcompanion.game.turn.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VotesBean(
    val votes: List<VoteBean> = emptyList()
) : Parcelable

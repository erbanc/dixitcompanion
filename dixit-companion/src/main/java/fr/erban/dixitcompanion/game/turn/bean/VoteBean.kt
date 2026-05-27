package fr.erban.dixitcompanion.game.turn.bean

import android.os.Parcelable
import fr.erban.dixitcompanion.game.player.PlayerBean
import kotlinx.parcelize.Parcelize

@Parcelize
data class VoteBean(
    val voter: PlayerBean,
    val elected: PlayerBean
) : Parcelable

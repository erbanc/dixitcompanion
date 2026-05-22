package fr.erban.dxitcompanion.game.turn.bean

import android.os.Parcelable
import fr.erban.dxitcompanion.game.player.PlayerBean
import kotlinx.parcelize.Parcelize

@Parcelize
data class VoteBean(
    val voter: PlayerBean,
    val elected: PlayerBean
) : Parcelable

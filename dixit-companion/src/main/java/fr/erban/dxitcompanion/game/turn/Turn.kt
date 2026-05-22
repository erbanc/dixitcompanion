package fr.erban.dxitcompanion.game.turn

import android.os.Parcelable
import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.game.turn.bean.VoteBean
import kotlinx.parcelize.Parcelize

@Parcelize
data class Turn(
    val storyTeller: PlayerBean? = null,
    val votes: List<VoteBean> = emptyList(),
    val noOneFound: Boolean = false,
    val everybodyFound: Boolean = false
) : Parcelable

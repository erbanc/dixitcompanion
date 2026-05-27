package fr.erban.dixitcompanion.game.turn

import android.os.Parcelable
import fr.erban.dixitcompanion.game.player.PlayerBean
import fr.erban.dixitcompanion.game.turn.bean.VoteBean
import kotlinx.parcelize.Parcelize

@Parcelize
data class Turn(
    val storyTeller: PlayerBean? = null,
    val votes: List<VoteBean> = emptyList(),
    val whoFound: List<PlayerBean> = emptyList(),
    val noOneFound: Boolean = false,
    val everybodyFound: Boolean = false
) : Parcelable

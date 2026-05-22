package fr.erban.dxitcompanion.db.game

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val idGame: Int = 0,
    val pointsToWin: Int,
    val finished: Boolean,
    val nameWinner: String?,
    val nbTurns: Int,
    val scoreSheet: String?
)

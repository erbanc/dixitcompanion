package fr.erban.dxitcompanion.db.player

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayerEntity(
    @PrimaryKey
    val name: String,
    val nbGames: Int = 0,
    val nbWins: Int = 0,
    val colorHex: String = "#5C3A9E",
    val emoji: String = "🎭"
)

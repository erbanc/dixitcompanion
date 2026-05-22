package fr.erban.dxitcompanion.db.crossref

import androidx.room.Entity

@Entity(primaryKeys = ["idGame", "name"])
data class GamePlayerCrossRefEntity(
    val idGame: Int,
    val name: String
)

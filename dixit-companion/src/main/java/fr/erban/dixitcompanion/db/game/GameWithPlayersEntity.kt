package fr.erban.dixitcompanion.db.game

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import fr.erban.dixitcompanion.db.crossref.GamePlayerCrossRefEntity
import fr.erban.dixitcompanion.db.player.PlayerEntity

data class GameWithPlayersEntity(
    @Embedded val game: GameEntity,
    @Relation(
        parentColumn = "idGame",
        entityColumn = "name",
        associateBy = Junction(GamePlayerCrossRefEntity::class)
    )
    val players: List<PlayerEntity>
)

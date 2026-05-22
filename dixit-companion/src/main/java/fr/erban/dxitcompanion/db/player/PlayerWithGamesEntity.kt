package fr.erban.dxitcompanion.db.player

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import fr.erban.dxitcompanion.db.crossref.GamePlayerCrossRefEntity
import fr.erban.dxitcompanion.db.game.GameEntity

data class PlayerWithGamesEntity(
    @Embedded val player: PlayerEntity,
    @Relation(
        parentColumn = "name",
        entityColumn = "idGame",
        associateBy = Junction(GamePlayerCrossRefEntity::class)
    )
    val games: List<GameEntity>
)

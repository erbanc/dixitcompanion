package fr.erban.dxitcompanion.db.player;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import fr.erban.dxitcompanion.db.crossref.GamePlayerCrossRefEntity;
import fr.erban.dxitcompanion.db.game.GameEntity;

public class PlayerWithGamesEntity {

    @Embedded
    public PlayerEntity player;

    @Relation(
            parentColumn = "name",
            entityColumn = "idGame",
            associateBy = @Junction(GamePlayerCrossRefEntity.class)
    )
    public List<GameEntity> games;
}

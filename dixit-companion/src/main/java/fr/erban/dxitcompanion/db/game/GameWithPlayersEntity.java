package fr.erban.dxitcompanion.db.game;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import fr.erban.dxitcompanion.db.crossref.GamePlayerCrossRefEntity;
import fr.erban.dxitcompanion.db.player.PlayerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameWithPlayersEntity {

    @Embedded
    public GameEntity game;

    @Relation(parentColumn = "idGame",
            entityColumn = "name",
            associateBy = @Junction(GamePlayerCrossRefEntity.class))
    public List<PlayerEntity> players;
}

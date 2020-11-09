package fr.erban.dxitcompanion.db.game;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity {

    @PrimaryKey(autoGenerate = true)
    private int idGame;

    private int pointsToWin;

    private boolean finished;

    private String nameWinner;

    private int nbTurns;

    private String scoreSheet;
}

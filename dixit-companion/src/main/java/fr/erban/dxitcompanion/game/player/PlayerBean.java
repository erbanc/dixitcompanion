package fr.erban.dxitcompanion.game.player;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
public class PlayerBean implements Serializable {

    private String name;

    private int currentScore;

    private int scoreLastTurn;

    private List<TurnScore> scoresheet;

    private int nbGames;

    private int nbWins;

    private boolean persisted;
}

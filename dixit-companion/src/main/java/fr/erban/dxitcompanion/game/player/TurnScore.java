package fr.erban.dxitcompanion.game.player;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TurnScore implements Serializable {

    private int turn;

    private int score;
}

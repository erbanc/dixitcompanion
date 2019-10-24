package fr.erban.dxitcompanion.game;

import fr.erban.dxitcompanion.game.player.Player;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Game implements Serializable {

    private List<Player> players;

    private int currentTurn;

    private int pointsToWin;
}

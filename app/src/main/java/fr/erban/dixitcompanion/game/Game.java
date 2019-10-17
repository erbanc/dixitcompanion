package fr.erban.dixitcompanion.game;

import java.io.Serializable;
import java.util.List;

import fr.erban.dixitcompanion.game.player.Player;
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

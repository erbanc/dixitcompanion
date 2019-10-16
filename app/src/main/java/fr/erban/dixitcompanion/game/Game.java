package fr.erban.dixitcompanion.game;

import java.util.List;

import fr.erban.dixitcompanion.game.player.Player;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Game {

    private List<Player> players;

    private int currentTurn;
}

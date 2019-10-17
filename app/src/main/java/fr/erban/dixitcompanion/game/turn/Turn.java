package fr.erban.dixitcompanion.game.turn;

import java.io.Serializable;

import fr.erban.dixitcompanion.game.player.Player;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Turn implements Serializable {

    private boolean noOneFound;

    private boolean everybodyFound;

    private Player storyTeller;
}

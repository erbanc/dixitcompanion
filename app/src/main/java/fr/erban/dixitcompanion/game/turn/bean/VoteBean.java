package fr.erban.dixitcompanion.game.turn.bean;

import fr.erban.dixitcompanion.game.player.Player;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VoteBean {

    private Player voter;

    private Player elected;
}

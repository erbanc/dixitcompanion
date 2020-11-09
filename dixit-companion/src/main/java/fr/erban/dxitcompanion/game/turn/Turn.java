package fr.erban.dxitcompanion.game.turn;

import java.io.Serializable;
import java.util.List;

import fr.erban.dxitcompanion.game.player.PlayerBean;
import fr.erban.dxitcompanion.game.turn.bean.VoteBean;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Turn implements Serializable {

    private boolean noOneFound;

    private boolean everybodyFound;

    private PlayerBean storyTeller;

    private List<VoteBean> votes;
}

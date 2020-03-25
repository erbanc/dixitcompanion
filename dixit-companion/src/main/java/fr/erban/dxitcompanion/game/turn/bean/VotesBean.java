package fr.erban.dxitcompanion.game.turn.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class VotesBean implements Serializable {

    private List<VoteBean> votes;
}

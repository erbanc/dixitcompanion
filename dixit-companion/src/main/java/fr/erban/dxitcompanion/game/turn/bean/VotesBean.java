package fr.erban.dxitcompanion.game.turn.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VotesBean implements Serializable {

    private List<VoteBean> votes;
}

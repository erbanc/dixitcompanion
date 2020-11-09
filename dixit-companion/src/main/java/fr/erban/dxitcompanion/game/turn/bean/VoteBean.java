package fr.erban.dxitcompanion.game.turn.bean;

import java.io.Serializable;

import fr.erban.dxitcompanion.game.player.PlayerBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteBean implements Serializable {

    private PlayerBean voter;

    private PlayerBean elected;
}

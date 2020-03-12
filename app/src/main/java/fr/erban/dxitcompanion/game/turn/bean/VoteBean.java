package fr.erban.dxitcompanion.game.turn.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import fr.erban.dxitcompanion.game.player.Player;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteBean implements Serializable {

    private Player voter;

    private Player elected;
}

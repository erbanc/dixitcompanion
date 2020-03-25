package fr.erban.dxitcompanion.game.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Player implements Serializable {

    private String name;

    private int currentScore;

    private List<TurnScore> scoresheet;
}

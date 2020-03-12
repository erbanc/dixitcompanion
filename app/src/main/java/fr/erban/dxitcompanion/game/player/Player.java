package fr.erban.dxitcompanion.game.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

import fr.erban.dxitcompanion.game.color.ColorEnum;

@Getter
@Builder
@AllArgsConstructor
public class Player implements Serializable {

    private String name;

    private int currentScore;

    private Map<Integer, Integer> scoreSheet;

    private ColorEnum color;
}

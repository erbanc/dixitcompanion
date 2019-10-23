package fr.erban.dxitcompanion.game.player;

import java.io.Serializable;
import java.util.Map;

import fr.erban.dxitcompanion.game.color.ColorEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Player implements Serializable {

    private String name;

    private int currentScore;

    private Map<Integer, Integer> scoreSheet;

    private ColorEnum color;
}

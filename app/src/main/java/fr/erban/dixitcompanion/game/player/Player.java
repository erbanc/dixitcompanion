package fr.erban.dixitcompanion.game.player;

import java.util.Map;

import fr.erban.dixitcompanion.game.color.ColorEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Player {

    private String name;

    private int currentScore;

    private boolean isStoryTeller;

    private Map<Integer, Integer> scoreSheet;

    private ColorEnum color;
}

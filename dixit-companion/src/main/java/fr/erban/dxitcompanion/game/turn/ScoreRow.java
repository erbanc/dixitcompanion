package fr.erban.dxitcompanion.game.turn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ScoreRow {

    private String name;

    private String score;

}

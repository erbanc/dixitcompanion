package fr.erban.dxitcompanion.game.turn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SelectPlayerRow {

    private String name;

    private boolean checked;

}

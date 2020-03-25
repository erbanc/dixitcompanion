package fr.erban.dxitcompanion.game;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Creator implements Serializable {

    private String userId;
}

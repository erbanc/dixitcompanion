package fr.erban.dxitcompanion.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.erban.dxitcompanion.game.player.PlayerBean;
import fr.erban.dxitcompanion.game.player.TurnScore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GameBean implements Serializable {

    private List<PlayerBean> players;

    private int currentTurn;

    private int pointsToWin;

    private boolean finished;

    private String nameWinner;

    public Map<String, Map<String, Integer>> getScoresheet() {
        final Map<String, Map<String, Integer>> scoresheet = new HashMap<>();

        for (final PlayerBean player : players) {

            final List<TurnScore> scorePerTurn = player.getScoresheet();
            final HashMap<String, Integer> scoresheetPlayer = new HashMap<>();
            for (final TurnScore turnScore : scorePerTurn) {
                scoresheetPlayer.put(String.valueOf(turnScore.getTurn()), turnScore.getScore());
            }
            scoresheet.put(player.getName(), scoresheetPlayer);
        }

        return scoresheet;
    }
}

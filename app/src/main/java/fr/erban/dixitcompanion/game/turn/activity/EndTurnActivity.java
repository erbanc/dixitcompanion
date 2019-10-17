package fr.erban.dixitcompanion.game.turn.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.erban.dixitcompanion.R;
import fr.erban.dixitcompanion.game.Game;
import fr.erban.dixitcompanion.game.player.Player;
import fr.erban.dixitcompanion.game.turn.Turn;
import fr.erban.dixitcompanion.game.turn.bean.VoteBean;

public class EndTurnActivity extends AppCompatActivity {

    private Turn turn;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_turn);

        this.turn = (Turn) getIntent().getSerializableExtra("Turn");
        this.game = (Game) getIntent().getSerializableExtra("Game");

        if (game != null) {
            List<Player> players = game.getPlayers();
            List<Player> playersWithUpdatedScores = new ArrayList<>();

            for (Player player : players) {
                final Player playerWithUpdatedScore = getPlayerResults(player, turn.getVotes());
                playersWithUpdatedScores.add(playerWithUpdatedScore);
            }

            game.setPlayers(playersWithUpdatedScores);
        }
    }

    private Player getPlayerResults(Player player, List<VoteBean> votes) {

        int votesForCard = 0;

        boolean hasFoundCard = false;

        if (votes != null) {
            for (VoteBean vote : votes) {
                if (vote.getElected().getName().equals(player.getName())) {
                    votesForCard++;
                }
                if (vote.getVoter().getName().equals(player.getName())
                        && vote.getElected().getName().equals(turn.getStoryTeller().getName())) {
                    hasFoundCard = true;
                }
            }
        }

        int updatedScore = player.getCurrentScore() + getPointsForTheTurn(player, votesForCard, hasFoundCard, turn);

        Map<Integer, Integer> newScoreSheet = player.getScoreSheet();
        newScoreSheet.put(game.getCurrentTurn(), updatedScore);


        return Player.builder()
                .color(player.getColor())
                .currentScore(updatedScore)
                .name(player.getName())
                .scoreSheet(newScoreSheet)
                .build();
    }

    private int getPointsForTheTurn(final Player player, final int numberOfVotes, final boolean hasFoundCard, final Turn turn) {

        int pointsGranted = 0;

        if (turn.getStoryTeller().equals(player)) {

            if (turn.isNoOneFound() || turn.isEverybodyFound()) {
                pointsGranted = 0;
            } else {
                pointsGranted = 3;
            }
        } else {
            if (turn.isNoOneFound() || turn.isEverybodyFound()) {
                pointsGranted += 2;
            } else if (hasFoundCard) {
                pointsGranted += 3;
            }

            pointsGranted += numberOfVotes;
        }

        return pointsGranted;
    }
}

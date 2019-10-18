package fr.erban.dixitcompanion.game.turn.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.erban.dixitcompanion.R;
import fr.erban.dixitcompanion.game.Game;
import fr.erban.dixitcompanion.game.player.Player;
import fr.erban.dixitcompanion.game.turn.CustomListAdapter;
import fr.erban.dixitcompanion.game.turn.ScoreRow;
import fr.erban.dixitcompanion.game.turn.Turn;
import fr.erban.dixitcompanion.game.turn.bean.VoteBean;

public class EndTurnActivity extends Activity {

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

            final ListView listView = findViewById(R.id.listViewEndTurn);

            List<ScoreRow> scores = new ArrayList<>();

            for (Player player : playersWithUpdatedScores) {
                scores.add(ScoreRow.builder().name(player.getName()).score(String.valueOf(player.getCurrentScore())).build());
            }

            CustomListAdapter adapter = new CustomListAdapter(this, scores);
            listView.setAdapter(adapter);
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

        if (turn.getStoryTeller().getName().equals(player.getName())) {

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

    public void continueToSelectStoryTeller(View view) {

    }
}

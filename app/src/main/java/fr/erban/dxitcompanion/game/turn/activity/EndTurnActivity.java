package fr.erban.dxitcompanion.game.turn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.erban.dxitcompanion.MainActivity;
import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.Game;
import fr.erban.dxitcompanion.game.player.Player;
import fr.erban.dxitcompanion.game.turn.ScoreRow;
import fr.erban.dxitcompanion.game.turn.Turn;
import fr.erban.dxitcompanion.game.turn.adapter.PointsTotalAdapter;
import fr.erban.dxitcompanion.game.turn.bean.VoteBean;

public class EndTurnActivity extends Activity {

    private Turn turn;

    private Game game;

    private boolean scoreLimitReached = false;

    private Player winner;

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
                scores.add(ScoreRow.builder()
                        .name(player.getName())
                        .score(String.valueOf(player.getCurrentScore()))
                        .build());
            }

            PointsTotalAdapter adapter = new PointsTotalAdapter(this, scores);
            listView.setAdapter(adapter);

            final TextView turnNumber = findViewById(R.id.turnNumber);
            final String turnNumberComplete = getString(R.string.turnNumberPrefix) + game.getCurrentTurn();
            turnNumber.setText(turnNumberComplete);

            for (Player player : game.getPlayers()) {
                if (player.getCurrentScore() >= game.getPointsToWin()) {
                    if (winner == null || player.getCurrentScore() > winner.getCurrentScore()) {
                        winner = player;
                        scoreLimitReached = true;
                    }
                }
            }

            if (scoreLimitReached) {
                final TextView textPlayerWins = findViewById(R.id.endTurn);
                final String playerHasWon = winner.getName() + " a remporté la partie !";
                textPlayerWins.setText(playerHasWon);
                final TextView continueButton = findViewById(R.id.EndTurnContinueButton);
                continueButton.setText(R.string.EndGameButtonText);
            }
        }
    }

    private Player getPlayerResults(Player player, List<VoteBean> votes) {

        int votesForCard = 0;

        boolean hasFoundCard = false;

        if (votes != null) {
            for (VoteBean vote : votes) {
                if (vote.getElected()
                        .getName()
                        .equals(player.getName())) {
                    votesForCard++;
                }
                if (vote.getVoter()
                        .getName()
                        .equals(player.getName()) && vote.getElected()
                        .getName()
                        .equals(turn.getStoryTeller()
                                .getName())) {
                    hasFoundCard = true;
                }
            }
            // if no one has voted for the storyteller card
            if (player.getName()
                    .equals(turn.getStoryTeller()
                            .getName()) && votesForCard == 0) {
                turn.setNoOneFound(true);
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

        if (turn.getStoryTeller()
                .getName()
                .equals(player.getName())) {

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

    public void continueToEndOrNewTurn(View view) {

        if (scoreLimitReached) {
            continueToEndGame();
        } else {
            continueToNewTurn();
        }
    }

    private void continueToEndGame() {

        Intent intent = new Intent(EndTurnActivity.this, MainActivity.class);
        EndTurnActivity.this.startActivity(intent);
    }

    private void continueToNewTurn() {

        Intent intent = new Intent(EndTurnActivity.this, SelectStoryTellerActivity.class);
        intent.putExtra("Game", game);
        EndTurnActivity.this.startActivity(intent);
    }
}

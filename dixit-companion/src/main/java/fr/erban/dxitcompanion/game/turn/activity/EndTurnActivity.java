package fr.erban.dxitcompanion.game.turn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.db.CollectionsEnum;
import fr.erban.dxitcompanion.game.Game;
import fr.erban.dxitcompanion.game.activity.ScoresResultActivity;
import fr.erban.dxitcompanion.game.player.Player;
import fr.erban.dxitcompanion.game.player.TurnScore;
import fr.erban.dxitcompanion.game.turn.ScoreRow;
import fr.erban.dxitcompanion.game.turn.Turn;
import fr.erban.dxitcompanion.game.turn.adapter.PointsTotalAdapter;
import fr.erban.dxitcompanion.game.turn.bean.VoteBean;

public class EndTurnActivity extends Activity {

    private Turn turn;

    private Game game;

    private boolean scoreLimitReached = false;

    private Player winner;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_turn);

        db = FirebaseFirestore.getInstance();

        this.turn = (Turn) getIntent().getSerializableExtra("Turn");
        this.game = (Game) getIntent().getSerializableExtra("Game");

        if (game != null) {
            List<Player> players = game.getPlayers();
            List<Player> playersWithUpdatedScores = new ArrayList<>();

            for (Player player : players) {
                final Player playerWithUpdatedScore = getPlayerResults(player, turn.getVotes());
                playersWithUpdatedScores.add(playerWithUpdatedScore);
            }

            // Sort by score
            final Comparator<Player> playerComparatorByScore =
                    (Player player1, Player player2) ->
                            Integer.compare(player2.getCurrentScore(), player1.getCurrentScore());
            Collections.sort(playersWithUpdatedScores, playerComparatorByScore);

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
                final String playerHasWon = winner.getName() + getString(R.string.wonTheGame);
                textPlayerWins.setText(playerHasWon);
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
        }

        int updatedScore = player.getCurrentScore() + getPointsForTheTurn(player, votesForCard, hasFoundCard, turn);

        List<TurnScore> scoresheet = player.getScoresheet();
        scoresheet.add(TurnScore.builder().score(updatedScore).turn(game.getCurrentTurn()).build());

        return Player.builder()
                .currentScore(updatedScore)
                .name(player.getName())
                .scoresheet(scoresheet)
                .build();
    }

    private int getPointsForTheTurn(final Player player, final int numberOfVotes, final boolean hasFoundCard, final Turn turn) {

        int pointsGranted = 0;

        if (turn.getStoryTeller()
                .getName()
                .equals(player.getName())) {

            if (turn.isNoOneFound() || turn.isEverybodyFound()) {
                // If no one or every one found, the storyteller has no points
                pointsGranted = 0;
            } else {
                // else, he receives 3 points
                pointsGranted = 3;
            }
        } else {
            if (turn.isNoOneFound() || turn.isEverybodyFound()) {
                // If no one found or everybody found, the player receives 2 points
                pointsGranted += 2;
            } else if (hasFoundCard) {
                // else, he receives 3 if he found the card
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

        updateGame(true);

        Intent intent = new Intent(EndTurnActivity.this, ScoresResultActivity.class);
        intent.putExtra("Game", game);
        EndTurnActivity.this.startActivity(intent);
    }

    private void updateGame(boolean isItTheEnd) {
        final SharedPreferences sharedPreferences = getSharedPreferences("DixitCompanionSettings", Context.MODE_PRIVATE);
        final String currentGameId = sharedPreferences.getString("currentGameId", "noid");
        final DocumentReference gameToUpdate = db.collection(CollectionsEnum.GAMES.toString().toLowerCase()).document(currentGameId);
        final Map<String, Object> updatedGameToSave = new HashMap<>();
        updatedGameToSave.put("currentTurn", game.getCurrentTurn());
        updatedGameToSave.put("players", new Gson().toJson(game.getPlayers()));
        updatedGameToSave.put("isFinished", isItTheEnd);
        gameToUpdate.update(updatedGameToSave);
    }

    private void continueToNewTurn() {

        updateGame(false);

        Intent intent = new Intent(EndTurnActivity.this, SelectStoryTellerActivity.class);
        intent.putExtra("Game", game);
        EndTurnActivity.this.startActivity(intent);
    }
}

package fr.erban.dxitcompanion.game.turn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.db.game.GameViewModel;
import fr.erban.dxitcompanion.db.player.PlayerConverter;
import fr.erban.dxitcompanion.db.player.PlayerViewModel;
import fr.erban.dxitcompanion.game.GameBean;
import fr.erban.dxitcompanion.game.activity.ScoresResultActivity;
import fr.erban.dxitcompanion.game.player.PlayerBean;
import fr.erban.dxitcompanion.game.player.TurnScore;
import fr.erban.dxitcompanion.game.turn.ScoreRow;
import fr.erban.dxitcompanion.game.turn.Turn;
import fr.erban.dxitcompanion.game.turn.adapter.PointsTotalAdapter;
import fr.erban.dxitcompanion.game.turn.bean.VoteBean;

public class EndTurnActivity extends AppCompatActivity {

    private Turn turn;

    private GameBean gameBean;

    private boolean endGameReached = false;

    private PlayerBean winner;

    private GameViewModel gameViewModel;

    private PlayerViewModel playerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_turn);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        this.turn = (Turn) getIntent().getSerializableExtra("Turn");
        this.gameBean = (GameBean) getIntent().getSerializableExtra("Game");

        if (gameBean != null) {
            List<PlayerBean> players = gameBean.getPlayers();
            List<PlayerBean> playersWithUpdatedScores = new ArrayList<>();

            for (PlayerBean player : players) {
                final PlayerBean playerWithUpdatedScore = getPlayerResults(player, turn.getVotes());
                playersWithUpdatedScores.add(playerWithUpdatedScore);
            }

            // Sort by score
            final Comparator<PlayerBean> playerComparatorByScore =
                    (PlayerBean player1, PlayerBean player2) ->
                            Integer.compare(player2.getCurrentScore(), player1.getCurrentScore());
            Collections.sort(playersWithUpdatedScores, playerComparatorByScore);

            gameBean.setPlayers(playersWithUpdatedScores);

            final ListView listView = findViewById(R.id.listViewEndTurn);

            List<ScoreRow> scores = new ArrayList<>();

            for (PlayerBean player : playersWithUpdatedScores) {
                scores.add(ScoreRow.builder()
                        .name(player.getName())
                        .score(String.valueOf(player.getCurrentScore()))
                        .build());
            }

            PointsTotalAdapter adapter = new PointsTotalAdapter(this, scores);
            listView.setAdapter(adapter);

            final TextView turnNumber = findViewById(R.id.turnNumber);
            final String turnNumberComplete = getString(R.string.turnNumberPrefix) + gameBean.getCurrentTurn();
            turnNumber.setText(turnNumberComplete);

            for (PlayerBean player : gameBean.getPlayers()) {
                if (player.getCurrentScore() >= gameBean.getPointsToWin()) {
                    if (winner == null || player.getCurrentScore() > winner.getCurrentScore()) {
                        winner = player;
                        endGameReached = true;
                    } else if (winner != null && player.getCurrentScore() == winner.getCurrentScore()) {
                        endGameReached = false;
                    }
                }
            }

            if (endGameReached) {
                final TextView textPlayerWins = findViewById(R.id.endTurn);
                final String playerHasWon = winner.getName() + " " + getString(R.string.wonTheGame);
                textPlayerWins.setText(playerHasWon);
            }
        }
    }

    private PlayerBean getPlayerResults(PlayerBean player, List<VoteBean> votes) {

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
        scoresheet.add(TurnScore.builder().score(updatedScore).turn(gameBean.getCurrentTurn()).build());

        return PlayerBean.builder()
                .currentScore(updatedScore)
                .name(player.getName())
                .scoresheet(scoresheet)
                .nbGames(player.getNbGames())
                .nbWins(player.getNbWins())
                .persisted(player.isPersisted())
                .build();
    }

    private int getPointsForTheTurn(final PlayerBean player, final int numberOfVotes, final boolean hasFoundCard, final Turn turn) {

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

        if (endGameReached) {
            continueToEndGame();
        } else {
            continueToNewTurn();
        }
    }

    private void continueToEndGame() {

        Intent intent = new Intent(EndTurnActivity.this, ScoresResultActivity.class);
        gameBean.setNameWinner(winner.getName());
        updatePlayersForEndGame();
        updateGameForEndGame();
        saveGameAndPlayers();
        intent.putExtra("Game", gameBean);
        EndTurnActivity.this.startActivity(intent);
    }

    private void updateGameForEndGame() {
        gameBean.setFinished(true);
    }

    private void updatePlayersForEndGame() {
        for (PlayerBean playerBean : gameBean.getPlayers()) {
            if (playerBean.getName().equals(winner.getName())) {
                playerBean.setNbWins(playerBean.getNbWins() + 1);
            }
            playerBean.setNbGames(playerBean.getNbGames() + 1);
        }
    }

    private void saveGameAndPlayers() {

        // save the game
        gameViewModel.insert(gameBean);

        // saveThePlayers
        playerViewModel.update(PlayerConverter.toEntities(gameBean.getPlayers()));
    }

    private void continueToNewTurn() {

        Intent intent = new Intent(EndTurnActivity.this, SelectStoryTellerActivity.class);
        intent.putExtra("Game", gameBean);
        EndTurnActivity.this.startActivity(intent);
    }
}

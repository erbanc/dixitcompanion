package fr.erban.dxitcompanion.game.turn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.Game;
import fr.erban.dxitcompanion.game.player.Player;
import fr.erban.dxitcompanion.game.turn.Turn;
import fr.erban.dxitcompanion.game.turn.bean.VoteBean;
import fr.erban.dxitcompanion.game.turn.bean.VotesBean;

public class SelectVotesActivity extends Activity {

    private Turn turn;

    private Game game;

    private VotesBean votes;

    private Player voter;

    private List<String> alreadyVoted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_votes);

        this.turn = (Turn) getIntent().getSerializableExtra("Turn");
        this.game = (Game) getIntent().getSerializableExtra("Game");
        this.votes = (VotesBean) getIntent().getSerializableExtra("Votes");

        alreadyVoted = new ArrayList<>();
        alreadyVoted.add(turn.getStoryTeller().getName());
        for (VoteBean vote : votes.getVotes()) {
            alreadyVoted.add(vote.getVoter().getName());
        }

        if (votes == null) {
            votes = VotesBean.builder().votes(new ArrayList<VoteBean>()).build();
        }

        final TextView turnNumber = findViewById(R.id.turnNumber);
        final String turnNumberComplete = getString(R.string.turnNumberPrefix) + game.getCurrentTurn();
        turnNumber.setText(turnNumberComplete);

        reinitActivity();
    }

    private void reinitActivity() {
        List<Player> players = game.getPlayers();

        for (Player player : players) {
            boolean hasVoted = false;
            for (String voted : alreadyVoted) {
                if (player.getName().equals(voted)) {
                    hasVoted = true;
                }
            }

            if (!hasVoted) {
                voter = player;
                TextView title = findViewById(R.id.selectVotesTitle);
                String text = "Pour qui a voté " + player.getName() + " ?";
                title.setText(text);

                if (alreadyVoted.size() == players.size() - 1) {
                    Button nextPlayerBtn = findViewById(R.id.nextPlayerButton);
                    nextPlayerBtn.setEnabled(false);
                    Button continueBtn = findViewById(R.id.selectVotesContinueBtn);
                    continueBtn.setEnabled(true);
                }

                addPlayerNames(game);
                return;
            }
        }
    }

    private void addPlayerNames(Game game) {
        Spinner spinner = findViewById(R.id.selectVoteDropdown);

        List<Player> players = game.getPlayers();

        List<String> playerNames = new ArrayList<>();

        for (Player player : players) {
            if (!player.getName().equals(turn.getStoryTeller().getName()) && !player.getName().equals(voter.getName())) {
                playerNames.add(player.getName());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectVotesActivity.this,
                android.R.layout.simple_spinner_item, playerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void nextPlayer(View view) {
        alreadyVoted.add(voter.getName());

        ajouterVoteEnCours();

        reinitActivity();
    }


    private void ajouterVoteEnCours() {
        final VoteBean vote = new VoteBean();

        final Spinner spinner = findViewById(R.id.selectVoteDropdown);

        final String selectedPlayer = spinner.getSelectedItem().toString();

        final List<Player> players = game.getPlayers();

        for (Player player : players) {
            if (player.getName().equals(selectedPlayer)) {
                vote.setElected(player);
            }
        }

        vote.setVoter(voter);

        votes.getVotes().add(vote);
    }

    public void continueToEndTurn(View view) {

        ajouterVoteEnCours();

        turn.setVotes(votes.getVotes());

        Intent intent = new Intent(SelectVotesActivity.this, EndTurnActivity.class);
        intent.putExtra("Game", game);
        intent.putExtra("Turn", turn);
        SelectVotesActivity.this.startActivity(intent);
    }
}

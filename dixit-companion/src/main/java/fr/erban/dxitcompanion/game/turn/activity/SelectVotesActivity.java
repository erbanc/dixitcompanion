package fr.erban.dxitcompanion.game.turn.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.GameBean;
import fr.erban.dxitcompanion.game.player.PlayerBean;
import fr.erban.dxitcompanion.game.turn.Turn;
import fr.erban.dxitcompanion.game.turn.bean.VoteBean;
import fr.erban.dxitcompanion.game.turn.bean.VotesBean;

public class SelectVotesActivity extends AppCompatActivity {

    private Turn turn;

    private GameBean gameBean;

    private VotesBean votes;

    private PlayerBean voter;

    private List<String> alreadyVoted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_votes);

        this.turn = (Turn) getIntent().getSerializableExtra("Turn");
        this.gameBean = (GameBean) getIntent().getSerializableExtra("Game");
        this.votes = (VotesBean) getIntent().getSerializableExtra("Votes");

        alreadyVoted = new ArrayList<>();
        alreadyVoted.add(turn.getStoryTeller()
                .getName());
        for (VoteBean vote : votes.getVotes()) {
            alreadyVoted.add(vote.getVoter()
                    .getName());
        }

        if (votes == null) {
            votes = VotesBean.builder()
                    .votes(new ArrayList<>())
                    .build();
        }

        final TextView turnNumber = findViewById(R.id.turnNumber);
        final String turnNumberComplete = getString(R.string.turnNumberPrefix) + gameBean.getCurrentTurn();
        turnNumber.setText(turnNumberComplete);

        reinitActivity();
    }

    private void reinitActivity() {

        List<PlayerBean> players = gameBean.getPlayers();

        for (PlayerBean player : players) {
            boolean hasVoted = false;
            for (String voted : alreadyVoted) {
                if (player.getName()
                        .equals(voted)) {
                    hasVoted = true;
                    break;
                }
            }

            if (!hasVoted) {
                voter = player;
                TextView title = findViewById(R.id.selectVotesTitle);
                String text = "Pour qui a voté " + player.getName() + " ?";
                title.setText(text);
                final Typeface typeface = ResourcesCompat.getFont(this, R.font.write_me_a_song);
                title.setTypeface(typeface);
                title.setTextSize(60);

                if (alreadyVoted.size() == players.size() - 1) {
                    Button nextPlayerBtn = findViewById(R.id.nextPlayerButton);
                    nextPlayerBtn.setVisibility(View.INVISIBLE);
                    Button continueBtn = findViewById(R.id.selectVotesContinueBtn);
                    continueBtn.setVisibility(View.VISIBLE);
                }

                addPlayerNames(gameBean);
                return;
            }
        }
    }

    private void addPlayerNames(GameBean gameBean) {

        final List<PlayerBean> players = gameBean.getPlayers();
        final RadioGroup radioGroup = findViewById(R.id.radioGroupSelectVote);
        int count = radioGroup.getChildCount();
        if (count > 0) {
            for (int i = count - 1; i >= 0; i--) {
                View o = radioGroup.getChildAt(i);
                if (o instanceof RadioButton) {
                    radioGroup.removeViewAt(i);
                }
            }
        }
        radioGroup.clearCheck();

        for (PlayerBean player : players) {
            if (!player.getName()
                    .equals(turn.getStoryTeller()
                            .getName()) && !player.getName()
                    .equals(voter.getName())) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(player.getName());
                radioButton.setTextSize(50);
                final Typeface font = ResourcesCompat.getFont(this, R.font.write_me_a_song);
                radioButton.setTypeface(font);
                radioButton.setTextColor(getResources().getColor(R.color.backgroundTextColor));
                radioGroup.addView(radioButton);
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    radioButton.setChecked(true);
                }
            }
        }
    }

    public void nextPlayer(View view) {

        alreadyVoted.add(voter.getName());

        ajouterVoteEnCours();

        reinitActivity();
    }

    private void ajouterVoteEnCours() {

        final VoteBean vote = new VoteBean();

        final RadioGroup radioGroup = findViewById(R.id.radioGroupSelectVote);

        final int selectedPlayerId = radioGroup.getCheckedRadioButtonId();
        final View rb = radioGroup.findViewById(selectedPlayerId);
        int idx = radioGroup.indexOfChild(rb);
        final RadioButton radioButton = (RadioButton) radioGroup.getChildAt(idx);
        final String selectedPlayer = radioButton.getText().toString();

        final List<PlayerBean> players = gameBean.getPlayers();

        for (PlayerBean player : players) {
            if (player.getName()
                    .equals(selectedPlayer)) {
                vote.setElected(player);
            }
        }

        vote.setVoter(voter);

        votes.getVotes()
                .add(vote);
    }

    public void continueToEndTurn(View view) {

        ajouterVoteEnCours();

        turn.setVotes(votes.getVotes());

        Intent intent = new Intent(SelectVotesActivity.this, EndTurnActivity.class);
        intent.putExtra("Game", gameBean);
        intent.putExtra("Turn", turn);
        SelectVotesActivity.this.startActivity(intent);
    }
}

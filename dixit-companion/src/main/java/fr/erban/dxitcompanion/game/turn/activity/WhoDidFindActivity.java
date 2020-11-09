package fr.erban.dxitcompanion.game.turn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.GameBean;
import fr.erban.dxitcompanion.game.player.PlayerBean;
import fr.erban.dxitcompanion.game.turn.SelectPlayerRow;
import fr.erban.dxitcompanion.game.turn.Turn;
import fr.erban.dxitcompanion.game.turn.adapter.PlayerSelectionAdapter;
import fr.erban.dxitcompanion.game.turn.bean.VoteBean;
import fr.erban.dxitcompanion.game.turn.bean.VotesBean;

public class WhoDidFindActivity extends AppCompatActivity {

    private Turn turn;

    private GameBean gameBean;

    private List<PlayerBean> playersWhoFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.who_did_find);

        this.turn = (Turn) getIntent().getSerializableExtra("Turn");
        this.gameBean = (GameBean) getIntent().getSerializableExtra("Game");

        final ListView listView = findViewById(R.id.listViewSelectPlayer);

        List<SelectPlayerRow> playerRows = new ArrayList<>();

        List<PlayerBean> players = gameBean.getPlayers();

        for (PlayerBean player : players) {
            if (!player.getName()
                    .equals(turn.getStoryTeller()
                            .getName())) {
                playerRows.add(SelectPlayerRow.builder()
                        .name(player.getName())
                        .checked(false)
                        .build());
            }
        }

        PlayerSelectionAdapter adapter = new PlayerSelectionAdapter(this, playerRows);
        listView.setAdapter(adapter);

        final TextView turnNumber = findViewById(R.id.turnNumber);
        final String turnNumberComplete = getString(R.string.turnNumberPrefix) + gameBean.getCurrentTurn();
        turnNumber.setText(turnNumberComplete);

        playersWhoFound = new ArrayList<>();
    }

    public void clickCheckbox(View view) {

        CheckBox checkBox = (CheckBox) view;

        RelativeLayout relativeLayout = (RelativeLayout) checkBox.getParent();
        TextView name = (TextView) relativeLayout.getChildAt(0);

        if (checkBox.isChecked()) {
            for (PlayerBean player1 : gameBean.getPlayers()) {
                if (player1.getName()
                        .equals(name.getText()
                                .toString())) {
                    playersWhoFound.add(player1);
                }
            }
        } else {
            Iterator<PlayerBean> iter = playersWhoFound.iterator();
            while (iter.hasNext()) {
                PlayerBean p = iter.next();
                if (p.getName()
                        .equals(name.getText()
                                .toString())) {
                    iter.remove();
                }
            }
        }
    }

    public void continueToSelectVotes(View view) {

        if (playersWhoFound.size() == gameBean.getPlayers().size() - 1) {
            continueToEndTurn();
        } else {
            continueToSelectVotes();
        }
    }

    private void continueToSelectVotes() {
        List<VoteBean> votes = new ArrayList<>();

        for (PlayerBean player : playersWhoFound) {
            final VoteBean vote = VoteBean.builder()
                    .elected(turn.getStoryTeller())
                    .voter(player)
                    .build();

            votes.add(vote);
        }

        if (playersWhoFound.isEmpty()) {
            // If no one found the card
            turn.setNoOneFound(true);
        }

        Intent intent = new Intent(WhoDidFindActivity.this, SelectVotesActivity.class);
        intent.putExtra("Game", gameBean);
        intent.putExtra("Turn", turn);
        intent.putExtra("Votes", VotesBean.builder()
                .votes(votes)
                .build());
        WhoDidFindActivity.this.startActivity(intent);
    }

    private void continueToEndTurn() {
        turn.setEverybodyFound(true);
        Intent intent = new Intent(WhoDidFindActivity.this, EndTurnActivity.class);
        intent.putExtra("Game", gameBean);
        intent.putExtra("Turn", turn);
        WhoDidFindActivity.this.startActivity(intent);
    }
}

package fr.erban.dxitcompanion.game.turn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.Game;
import fr.erban.dxitcompanion.game.player.Player;
import fr.erban.dxitcompanion.game.turn.SelectPlayerRow;
import fr.erban.dxitcompanion.game.turn.Turn;
import fr.erban.dxitcompanion.game.turn.adapter.PlayerSelectionAdapter;
import fr.erban.dxitcompanion.game.turn.bean.VoteBean;
import fr.erban.dxitcompanion.game.turn.bean.VotesBean;

public class WhoDidFindActivity extends Activity {

    private Turn turn;

    private Game game;

    private List<Player> playersWhoFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.who_did_find);

        this.turn = (Turn) getIntent().getSerializableExtra("Turn");
        this.game = (Game) getIntent().getSerializableExtra("Game");

        final ListView listView = findViewById(R.id.listViewSelectPlayer);

        List<SelectPlayerRow> playerRows = new ArrayList<>();

        List<Player> players = game.getPlayers();

        for (Player player : players) {
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
        final String turnNumberComplete = getString(R.string.turnNumberPrefix) + game.getCurrentTurn();
        turnNumber.setText(turnNumberComplete);

        playersWhoFound = new ArrayList<>();
    }

    public void clickCheckbox(View view) {

        CheckBox checkBox = (CheckBox) view;

        RelativeLayout relativeLayout = (RelativeLayout) checkBox.getParent();
        TextView name = (TextView) relativeLayout.getChildAt(0);

        if (checkBox.isChecked()) {
            for (Player player1 : game.getPlayers()) {
                if (player1.getName()
                        .equals(name.getText()
                                .toString())) {
                    playersWhoFound.add(player1);
                }
            }
        } else {
            Iterator<Player> iter = playersWhoFound.iterator();
            while (iter.hasNext()) {
                Player p = iter.next();
                if (p.getName()
                        .equals(name.getText()
                                .toString())) {
                    iter.remove();
                }
            }
        }
    }

    public void continueToSelectVotes(View view) {

        if (playersWhoFound.size() == game.getPlayers().size() -1) {
            continueToEndTurn();
        } else {
            continueToSelectVotes();
        }
    }

    private void continueToSelectVotes() {
        List<VoteBean> votes = new ArrayList<>();

        for (Player player : playersWhoFound) {
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
        intent.putExtra("Game", game);
        intent.putExtra("Turn", turn);
        intent.putExtra("Votes", VotesBean.builder()
                .votes(votes)
                .build());
        WhoDidFindActivity.this.startActivity(intent);
    }

    private void continueToEndTurn() {
        turn.setEverybodyFound(true);
        Intent intent = new Intent(WhoDidFindActivity.this, EndTurnActivity.class);
        intent.putExtra("Game", game);
        intent.putExtra("Turn", turn);
        WhoDidFindActivity.this.startActivity(intent);
    }
}

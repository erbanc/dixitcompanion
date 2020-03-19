package fr.erban.dxitcompanion.game.turn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.Game;
import fr.erban.dxitcompanion.game.player.Player;
import fr.erban.dxitcompanion.game.turn.Turn;

public class SelectStoryTellerActivity extends Activity {

    private Turn turn;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_storyteller);

        turn = Turn.builder()
                .build();

        this.game = (Game) getIntent().getSerializableExtra("Game");

        final Spinner spinner = findViewById(R.id.selectStorytellerDropdown);

        if (game != null) {
            game.setCurrentTurn(game.getCurrentTurn() + 1);
            addPlayerNames(game, spinner);
        }

        final TextView turnNumber = findViewById(R.id.turnNumber);
        final String turnNumberComplete = getString(R.string.turnNumberPrefix) + game.getCurrentTurn();
        turnNumber.setText(turnNumberComplete);

    }

    private void addPlayerNames(Game game, Spinner spinner) {

        final List<Player> players = game.getPlayers();

        final List<String> playerNames = new ArrayList<>();

        for (Player player : players) {
            playerNames.add(player.getName());
        }

        final ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SelectStoryTellerActivity.this, R.layout.spinner_item, playerNames);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }

    public void continueToTurnResults(View view) {

        final Spinner spinner = findViewById(R.id.selectStorytellerDropdown);

        final String selectedPlayer = spinner.getSelectedItem()
                .toString();

        final List<Player> players = game.getPlayers();

        for (Player player : players) {
            if (player.getName()
                    .equals(selectedPlayer)) {
                turn.setStoryTeller(player);
            }
        }

        Intent intent = new Intent(SelectStoryTellerActivity.this, EveryoneFoundActivity.class);
        intent.putExtra("Game", game);
        intent.putExtra("Turn", turn);
        SelectStoryTellerActivity.this.startActivity(intent);
    }
}

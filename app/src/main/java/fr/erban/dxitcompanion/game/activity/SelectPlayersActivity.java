package fr.erban.dxitcompanion.game.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.Game;
import fr.erban.dxitcompanion.game.player.Player;

public class SelectPlayersActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_players);
    }

    public void continueToNumberPoints(View view) {

        final List<Player> players = retrievePlayers();

        final Game game = Game.builder().players(players).currentTurn(0).build();

        Intent intent = new Intent(SelectPlayersActivity.this, SelectNumberPointsActivity.class);
        intent.putExtra("Game", game);
        SelectPlayersActivity.this.startActivity(intent);
    }

    /**
     * Get the names and the colors of the players
     *
     * @return the list of the players
     */
    private List<Player> retrievePlayers() {
        List<Player> players = new ArrayList<>();

        final EditText player1 = findViewById(R.id.Player1Name);

        addPlayerIfNotEmpty(player1, players);

        final EditText player2 = findViewById(R.id.Player2Name);

        addPlayerIfNotEmpty(player2, players);

        final EditText player3 = findViewById(R.id.Player3Name);

        addPlayerIfNotEmpty(player3, players);

        final EditText player4 = findViewById(R.id.Player4Name);

        addPlayerIfNotEmpty(player4, players);

        final EditText player5 = findViewById(R.id.Player5Name);

        addPlayerIfNotEmpty(player5, players);

        final EditText player6 = findViewById(R.id.Player6Name);

        addPlayerIfNotEmpty(player6, players);

        final EditText player7 = findViewById(R.id.Player7Name);

        addPlayerIfNotEmpty(player7, players);

        final EditText player8 = findViewById(R.id.Player8Name);

        addPlayerIfNotEmpty(player8, players);

        final EditText player9 = findViewById(R.id.Player9Name);

        addPlayerIfNotEmpty(player9, players);

        final EditText player10 = findViewById(R.id.Player10Name);

        addPlayerIfNotEmpty(player10, players);

        return players;
    }

    private void addPlayerIfNotEmpty(final EditText player, final List<Player> players) {
        if (player.getText() != null && !player.getText().toString().trim().isEmpty()) {
            players.add(Player.builder()
                    .name(player.getText().toString())
                    .currentScore(0)
                    .scoreSheet(new HashMap<Integer, Integer>())
                    .build());
        }
    }
}

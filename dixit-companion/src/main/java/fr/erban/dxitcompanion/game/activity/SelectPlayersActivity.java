package fr.erban.dxitcompanion.game.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.Creator;
import fr.erban.dxitcompanion.game.Game;
import fr.erban.dxitcompanion.game.player.Player;

public class SelectPlayersActivity extends Activity {

    private Creator creator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_players);

        creator = (Creator) getIntent().getSerializableExtra("Creator");

    }

    public void continueToNumberPoints(View view) {

        final List<Player> players = retrievePlayers();

        if (verifyNumberOfPlayers(players)) return;

        if (verifyDuplicateNames(players)) return;

        final Game game = Game.builder()
                .players(players)
                .currentTurn(0)
                .creator(creator)
                .uuid(UUID.randomUUID().toString())
                .build();

        Intent intent = new Intent(SelectPlayersActivity.this, SelectNumberPointsActivity.class);
        intent.putExtra("Game", game);
        SelectPlayersActivity.this.startActivity(intent);
    }

    private boolean verifyNumberOfPlayers(List<Player> players) {
        if (players.size() < 3) {
            final Context context = getApplicationContext();
            final String message = getString(R.string.notEnoughPlayers);
            int duration = Toast.LENGTH_SHORT;
            final Toast toast = Toast.makeText(context, message, duration);
            toast.show();
            return true;
        }
        return false;
    }

    private boolean verifyDuplicateNames(List<Player> players) {
        List<String> playerNames = new ArrayList<>();
        for (Player player : players) {
            for (String name : playerNames) {
                if (player.getName().equals(name)) {
                    final Context context = getApplicationContext();
                    final String message = getString(R.string.duplicatedName);
                    int duration = Toast.LENGTH_SHORT;
                    final Toast toast = Toast.makeText(context, message, duration);
                    toast.show();
                    return true;
                }
            }
            playerNames.add(player.getName());
        }
        return false;
    }

    /**
     * Get the names of the players
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

        if (player.getText() != null && !player.getText()
                .toString()
                .trim()
                .isEmpty()) {
            players.add(Player.builder()
                    .name(player.getText()
                            .toString())
                    .currentScore(0)
                    .scoresheet(new ArrayList<>())
                    .build());
        }
    }
}

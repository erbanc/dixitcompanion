package fr.erban.dxitcompanion.game.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.Creator;
import fr.erban.dxitcompanion.game.Game;
import fr.erban.dxitcompanion.game.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SelectPlayersActivity extends Activity {

    private Creator creator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_players);

        creator = (Creator) getIntent().getSerializableExtra("Creator");

        EditText myTextBox = findViewById(R.id.playerName);
        final ChipGroup chipGroupPlayers = findViewById(R.id.addedPlayers);
        final Button addPlayer = findViewById(R.id.addPlayerButton);
        addPlayer.setEnabled(false);
        myTextBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().trim().isEmpty()) {
                    addPlayer.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.toString().trim().isEmpty()) {
                    addPlayer.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isError = false;
                TextInputLayout layout = findViewById(R.id.textInputLayout);
                if (!s.toString().isEmpty() && s.toString().trim().isEmpty()) {
                    layout.setError(getString(R.string.emptyPlayer));
                    layout.setErrorEnabled(true);
                    isError = true;
                } else {
                    for (int i=0; i<chipGroupPlayers.getChildCount();i++){
                        Chip chip = (Chip)chipGroupPlayers.getChildAt(i);
                        if (chip.getText().toString().equals(s.toString().trim())) {
                            layout.setError(getString(R.string.playerAlreadyExists));
                            layout.setErrorEnabled(true);
                            isError = true;
                        }
                    }
                }
                if (!isError) {
                    addPlayer.setEnabled(true);
                    layout.setError("");
                    layout.setErrorEnabled(false);
                } else {
                    addPlayer.setEnabled(false);
                }
            }
        });
    }

    public void onAddPlayer(View view) {

        final Chip chip = new Chip(this);
        final TextView playerName = findViewById(R.id.playerName);

        if (playerName == null || playerName.getText().toString().trim().isEmpty()) {
            return;
        }
        final ChipGroup chipGroupPlayers = findViewById(R.id.addedPlayers);

        chip.setText(playerName.getText().toString().trim());
        chip.setCloseIconVisible(true);
        chip.setTextAppearanceResource(R.style.chipText);
        chip.setCheckable(false);
        chip.setChecked(true);
        chip.setOnCloseIconClickListener(v -> chipGroupPlayers.removeView(chip));
        chip.setHapticFeedbackEnabled(true);

        chipGroupPlayers.addView(chip);
        playerName.setText("");
    }

    public void continueToNumberPoints(View view) {

        final List<Player> players = retrievePlayers();

        if (verifyNumberOfPlayers(players)) return;

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

    /**
     * Get the names of the players
     *
     * @return the list of the players
     */
    private List<Player> retrievePlayers() {

        final List<Player> players = new ArrayList<>();
        final ChipGroup chipGroupPlayers = findViewById(R.id.addedPlayers);
        for (int i=0; i<chipGroupPlayers.getChildCount();i++){
            Chip chip = (Chip)chipGroupPlayers.getChildAt(i);
            if (chip.getText() != null && !chip.getText().toString().trim().isEmpty()) {
                players.add(Player.builder()
                        .currentScore(0)
                        .name(chip.getText().toString())
                        .scoresheet(new ArrayList<>())
                        .build());
            }
        }

        return players;
    }
}

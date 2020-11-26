package fr.erban.dxitcompanion.game.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.db.player.PlayerConverter;
import fr.erban.dxitcompanion.db.player.PlayerEntity;
import fr.erban.dxitcompanion.db.player.PlayerViewModel;
import fr.erban.dxitcompanion.game.GameBean;
import fr.erban.dxitcompanion.game.player.PlayerBean;

public class SelectPlayersActivity extends AppCompatActivity {

    private PlayerViewModel playerViewModel;

    private List<String> playersAlreadyInDb;

    private List<PlayerEntity> playerEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_players);

        AutoCompleteTextView myTextBox = findViewById(R.id.playerName);
        final ChipGroup chipGroupPlayers = findViewById(R.id.addedPlayers);
        final Button addPlayer = findViewById(R.id.addPlayerButton);
        addPlayer.setEnabled(false);
        myTextBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().trim().isEmpty()) {
                    addPlayer.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    addPlayer.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                afterTextChangedVerify(s, chipGroupPlayers, addPlayer);
            }
        });

        observePlayersInDbToSetSuggestions(myTextBox);
    }

    private void observePlayersInDbToSetSuggestions(AutoCompleteTextView myTextBox) {

        final Observer<List<PlayerEntity>> playersInDbObserver = playersInDb -> {
            // Update the UI, in this case, a TextView.
            playerEntities = playersInDb;
            final String[] nameSuggestions = new String[playersInDb.size()];
            playersAlreadyInDb = new ArrayList<>();
            for (int i = 0, playersInDbSize = playersInDb.size(); i < playersInDbSize; i++) {
                PlayerEntity playerInDb = playersInDb.get(i);
                nameSuggestions[i] = playerInDb.getName();
                playersAlreadyInDb.add(playerInDb.getName());
            }
            if (!playersInDb.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, nameSuggestions);
                myTextBox.setAdapter(adapter);

            }
        };

        playerViewModel.getPlayers().observe(this, playersInDbObserver);
    }

    private void afterTextChangedVerify(Editable s, ChipGroup chipGroupPlayers, Button addPlayer) {
        boolean isError = false;
        if (!s.toString().isEmpty() && s.toString().trim().isEmpty()) {
            isError = true;
        } else {
            for (int i = 0; i < chipGroupPlayers.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupPlayers.getChildAt(i);
                if (chip.getText().toString().toLowerCase().equals(s.toString().trim().toLowerCase())) {
                    isError = true;
                }
            }
        }
        addPlayer.setEnabled(!isError);
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

        final List<PlayerBean> players = retrievePlayersFromChips();

        if (verifyNumberOfPlayers(players)) return;

        final GameBean gameBean = GameBean.builder()
                .players(players)
                .currentTurn(0)
                .finished(false)
                .build();

        createNewPlayersInDatabase(players);

        Intent intent = new Intent(SelectPlayersActivity.this, SelectNumberPointsActivity.class);
        intent.putExtra("Game", gameBean);
        SelectPlayersActivity.this.startActivity(intent);
    }

    private void createNewPlayersInDatabase(List<PlayerBean> players) {

        for (PlayerBean player : players) {
            if (!player.isPersisted()) {
                // each new player is persisted in database
                playerViewModel.insert(PlayerConverter.toEntity(player));
                player.setPersisted(true);
            }
        }
    }

    private boolean verifyNumberOfPlayers(List<PlayerBean> players) {
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
     * @return the list of {@link PlayerBean}
     */
    private List<PlayerBean> retrievePlayersFromChips() {

        final List<PlayerBean> players = new ArrayList<>();
        final ChipGroup chipGroupPlayers = findViewById(R.id.addedPlayers);
        for (int i = 0; i < chipGroupPlayers.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupPlayers.getChildAt(i);
            final String playerName = chip.getText().toString();

            if (playersAlreadyInDb.contains(playerName)) {
                // if it is an existing player, the bean is converted from the entity
                PlayerEntity entity = new PlayerEntity();
                for (PlayerEntity playerEntity : playerEntities) {
                    if (playerEntity
                            .getName()
                            .equals(playerName)) {
                        entity = playerEntity;
                        break;
                    }
                }
                players.add(PlayerConverter.toBean(entity));
            } else {
                // if not, we create it from scratch
                players.add(PlayerBean.builder()
                        .currentScore(0)
                        .name(chip.getText().toString())
                        .scoresheet(new ArrayList<>())
                        .persisted(false)
                        .nbWins(0)
                        .nbGames(0)
                        .build());
            }
        }

        return players;
    }
}

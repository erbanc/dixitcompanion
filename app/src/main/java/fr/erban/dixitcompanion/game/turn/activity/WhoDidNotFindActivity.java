package fr.erban.dixitcompanion.game.turn.activity;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import fr.erban.dixitcompanion.R;
import fr.erban.dixitcompanion.game.Game;
import fr.erban.dixitcompanion.game.player.Player;
import fr.erban.dixitcompanion.game.turn.Turn;

public class WhoDidNotFindActivity extends Activity {

    private Turn turn;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.who_did_not_find);

        this.turn = (Turn) getIntent().getSerializableExtra("Turn");
        this.game = (Game) getIntent().getSerializableExtra("Game");

        if (game != null) {
            List<Player> players = game.getPlayers();
            for (Player player : players) {
                if (!player.getName().equals(turn.getStoryTeller().getName())) {
                    final ChipGroup chipGroup = findViewById(R.id.WhoDidNotFindChipGroup);
                    Chip chip = new Chip(this);
                    chip.setText(player.getName());
                    chip.setChipBackgroundColorResource(R.color.colorPrimary);
                    chip.setTextAppearanceResource(R.style.ChipTextStyle);

                    chipGroup.addView(chip);
                }
            }
        }
    }
}

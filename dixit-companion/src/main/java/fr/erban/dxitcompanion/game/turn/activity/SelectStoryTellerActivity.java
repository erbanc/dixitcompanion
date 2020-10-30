package fr.erban.dxitcompanion.game.turn.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.List;

import androidx.core.content.res.ResourcesCompat;
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

        final RadioGroup radioGroup = findViewById(R.id.radioGroupChooseStoryteller);

        if (game != null) {
            game.setCurrentTurn(game.getCurrentTurn() + 1);
            addPlayerNames(game, radioGroup);
        }

        final TextView turnNumber = findViewById(R.id.turnNumber);
        final String turnNumberComplete = getString(R.string.turnNumberPrefix) + game.getCurrentTurn();
        turnNumber.setText(turnNumberComplete);

    }

    private void addPlayerNames(Game game, RadioGroup radioGroup) {

        final List<Player> players = game.getPlayers();

        for (int i = 0, playersSize = players.size(); i < playersSize; i++) {
            Player player = players.get(i);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(player.getName());
            radioButton.setVisibility(View.VISIBLE);
            radioButton.setTextSize(50);
            final Typeface font = ResourcesCompat.getFont(this, R.font.write_me_a_song);
            radioButton.setTypeface(font);
            radioButton.setTextColor(R.color.backgroundTextColor);

            radioGroup.addView(radioButton);

            if (i == 0) {
                radioButton.setChecked(true);
            }
        }
    }

    public void continueToTurnResults(View view) {

        final RadioGroup radioGroup = findViewById(R.id.radioGroupChooseStoryteller);

        final int selectedPlayerId = radioGroup.getCheckedRadioButtonId();
        final View rb = radioGroup.findViewById(selectedPlayerId);
        int idx = radioGroup.indexOfChild(rb);
        final RadioButton radioButton = (RadioButton) radioGroup.getChildAt(idx);
        final String storyTellerName = radioButton.getText().toString();

        final List<Player> players = game.getPlayers();

        for (Player player : players) {
            if (player.getName()
                    .equals(storyTellerName)) {
                turn.setStoryTeller(player);
            }
        }

        Intent intent = new Intent(SelectStoryTellerActivity.this, EveryoneFoundActivity.class);
        intent.putExtra("Game", game);
        intent.putExtra("Turn", turn);
        SelectStoryTellerActivity.this.startActivity(intent);
    }
}

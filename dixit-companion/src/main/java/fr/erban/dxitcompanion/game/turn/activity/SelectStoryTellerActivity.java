package fr.erban.dxitcompanion.game.turn.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.List;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.GameBean;
import fr.erban.dxitcompanion.game.player.PlayerBean;
import fr.erban.dxitcompanion.game.turn.Turn;

public class SelectStoryTellerActivity extends AppCompatActivity {

    private Turn turn;

    private GameBean gameBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_storyteller);

        turn = Turn.builder()
                .build();

        this.gameBean = (GameBean) getIntent().getSerializableExtra("Game");

        final RadioGroup radioGroup = findViewById(R.id.radioGroupChooseStoryteller);

        if (gameBean != null) {
            gameBean.setCurrentTurn(gameBean.getCurrentTurn() + 1);
            addPlayerNames(gameBean, radioGroup);
        }

        final TextView turnNumber = findViewById(R.id.turnNumber);
        final String turnNumberComplete = getString(R.string.turnNumberPrefix) + gameBean.getCurrentTurn();
        turnNumber.setText(turnNumberComplete);

    }

    private void addPlayerNames(GameBean gameBean, RadioGroup radioGroup) {

        final List<PlayerBean> players = gameBean.getPlayers();

        for (int i = 0, playersSize = players.size(); i < playersSize; i++) {
            PlayerBean player = players.get(i);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(player.getName());
            radioButton.setVisibility(View.VISIBLE);
            radioButton.setTextSize(50);
            final Typeface font = ResourcesCompat.getFont(this, R.font.write_me_a_song);
            radioButton.setTypeface(font);
            radioButton.setTextColor(getResources().getColor(R.color.backgroundTextColor));

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

        final List<PlayerBean> players = gameBean.getPlayers();

        for (PlayerBean player : players) {
            if (player.getName()
                    .equals(storyTellerName)) {
                turn.setStoryTeller(player);
            }
        }

        Intent intent = new Intent(SelectStoryTellerActivity.this, EveryoneFoundActivity.class);
        intent.putExtra("Game", gameBean);
        intent.putExtra("Turn", turn);
        SelectStoryTellerActivity.this.startActivity(intent);
    }
}

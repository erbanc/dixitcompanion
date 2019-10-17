package fr.erban.dixitcompanion.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import fr.erban.dixitcompanion.R;
import fr.erban.dixitcompanion.game.turn.SelectStoryTellerActivity;

public class SelectNumberPointsActivity extends AppCompatActivity {

    final static String DEFAULT_POINTS_TO_WIN = "30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_points);

        final EditText pointsToWin = findViewById(R.id.pointsToWin);

        pointsToWin.setText(DEFAULT_POINTS_TO_WIN);
    }

    public void continueToFirstTurn(View view) {

        final EditText pointsToWin = findViewById(R.id.pointsToWin);

        final Game game = (Game) getIntent().getSerializableExtra("Game");

        if (game != null) {
            game.setPointsToWin(Integer.parseInt(pointsToWin.getText().toString()));
        }

        Intent intent = new Intent(SelectNumberPointsActivity.this, SelectStoryTellerActivity.class);
        intent.putExtra("Game", game);
        SelectNumberPointsActivity.this.startActivity(intent);
    }

}

package fr.erban.dixitcompanion.game.turn;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.erban.dixitcompanion.R;
import fr.erban.dixitcompanion.game.Game;

public class PointsResultActivity extends AppCompatActivity {

    private Turn turn;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_storyteller);

        this.turn = (Turn) getIntent().getSerializableExtra("Turn");
        this.game = (Game) getIntent().getSerializableExtra("Game");
    }
}

package fr.erban.dixitcompanion.game.turn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import fr.erban.dixitcompanion.R;
import fr.erban.dixitcompanion.game.Game;

public class EveryoneFoundActivity extends AppCompatActivity {

    private Turn turn;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.everyone_found);

        this.turn = (Turn) getIntent().getSerializableExtra("Turn");
        this.game = (Game) getIntent().getSerializableExtra("Game");
        if (game != null) {
            game.setCurrentTurn(game.getCurrentTurn() + 1);
        }
    }

    public void clickYes(View view) {
        this.turn.setEverybodyFound(true);
        endTurn();
    }

    public void clickNo(View view) {
        this.turn.setEverybodyFound(false);
        endTurn();
    }

    private void endTurn() {
        Intent intent = new Intent(EveryoneFoundActivity.this, EndTurnActivity.class);
        intent.putExtra("Game", game);
        intent.putExtra("Turn", turn);
        EveryoneFoundActivity.this.startActivity(intent);
    }
}

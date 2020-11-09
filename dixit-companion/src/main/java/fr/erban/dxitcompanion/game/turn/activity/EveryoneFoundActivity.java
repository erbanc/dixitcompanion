package fr.erban.dxitcompanion.game.turn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.GameBean;
import fr.erban.dxitcompanion.game.turn.Turn;

public class EveryoneFoundActivity extends AppCompatActivity {

    private Turn turn;

    private GameBean gameBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.everyone_found);

        this.turn = (Turn) getIntent().getSerializableExtra("Turn");
        this.gameBean = (GameBean) getIntent().getSerializableExtra("Game");

        final TextView turnNumber = findViewById(R.id.turnNumber);
        final String turnNumberComplete = getString(R.string.turnNumberPrefix) + gameBean.getCurrentTurn();
        turnNumber.setText(turnNumberComplete);
    }

    public void clickYes(View view) {

        this.turn.setEverybodyFound(true);
        endTurn();
    }

    public void clickNo(View view) {

        this.turn.setEverybodyFound(false);
        toWhoDidFind();
    }

    private void toWhoDidFind() {

        Intent intent = new Intent(EveryoneFoundActivity.this, WhoDidFindActivity.class);
        intent.putExtra("Game", gameBean);
        intent.putExtra("Turn", turn);
        EveryoneFoundActivity.this.startActivity(intent);
    }

    private void endTurn() {

        Intent intent = new Intent(EveryoneFoundActivity.this, EndTurnActivity.class);
        intent.putExtra("Game", gameBean);
        intent.putExtra("Turn", turn);
        EveryoneFoundActivity.this.startActivity(intent);
    }
}

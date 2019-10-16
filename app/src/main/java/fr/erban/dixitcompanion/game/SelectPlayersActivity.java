package fr.erban.dixitcompanion.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import fr.erban.dixitcompanion.R;

public class SelectPlayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_players);
    }

    public void continueToNumberPoints(View view) {

        Intent intent = new Intent(SelectPlayersActivity.this, SelectNumberPoints.class);
        SelectPlayersActivity.this.startActivity(intent);
    }
}

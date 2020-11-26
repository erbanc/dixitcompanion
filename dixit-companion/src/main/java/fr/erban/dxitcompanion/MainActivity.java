package fr.erban.dxitcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import fr.erban.dxitcompanion.game.activity.SelectPlayersActivity;
import fr.erban.dxitcompanion.rules.activity.RulesActivity;
import fr.erban.dxitcompanion.stats.activity.StatsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
    }

    public void startNewGame(View view) {

        // launch a new game
        Intent intent = new Intent(MainActivity.this, SelectPlayersActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToStats(View view) {

        // see the stats
        Intent intent = new Intent(MainActivity.this, StatsActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToRules(View view) {

        // see the rules
        Intent intent = new Intent(MainActivity.this, RulesActivity.class);
        MainActivity.this.startActivity(intent);
    }
}

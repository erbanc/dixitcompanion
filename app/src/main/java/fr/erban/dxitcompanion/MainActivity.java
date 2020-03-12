package fr.erban.dxitcompanion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import fr.erban.dxitcompanion.game.activity.SelectPlayersActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
    }

    public void startNewGame(View view) {

        // lance une nouvelle partie
        Intent intent = new Intent(MainActivity.this, SelectPlayersActivity.class);
        MainActivity.this.startActivity(intent);
    }
}

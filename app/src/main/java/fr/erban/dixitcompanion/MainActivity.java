package fr.erban.dixitcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import fr.erban.dixitcompanion.game.SelectPlayersActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startNewGame(View view) {

        // lance une nouvelle partie
        Intent intent = new Intent(MainActivity.this, SelectPlayersActivity.class);
        MainActivity.this.startActivity(intent);
    }
}

package fr.erban.dixitcompanion.game;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import fr.erban.dixitcompanion.R;

public class SelectNumberPoints extends AppCompatActivity {

    final static String DEFAULT_POINTS_TO_WIN = "30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_points);

        final EditText pointsToWin = findViewById(R.id.pointsToWin);

        pointsToWin.setText(DEFAULT_POINTS_TO_WIN);
    }

}

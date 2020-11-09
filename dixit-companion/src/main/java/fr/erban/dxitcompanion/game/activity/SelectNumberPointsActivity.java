package fr.erban.dxitcompanion.game.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.GameBean;
import fr.erban.dxitcompanion.game.turn.activity.SelectStoryTellerActivity;

public class SelectNumberPointsActivity extends AppCompatActivity {

    private final static String DEFAULT_POINTS_TO_WIN = "30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_points);

        final EditText pointsToWin = findViewById(R.id.pointsToWin);

        pointsToWin.setText(DEFAULT_POINTS_TO_WIN);
    }

    public void continueToFirstTurn(View view) {

        final EditText pointsToWin = findViewById(R.id.pointsToWin);

        final GameBean gameBean = (GameBean) getIntent().getSerializableExtra("Game");

        if (gameBean != null) {
            final String pointsEntered = pointsToWin.getText()
                    .toString();
            try {
                final int nbPointsToWin = Integer.parseInt(pointsEntered);
                if (nbPointsToWin == 0) {
                    throw new NumberFormatException();
                }
                gameBean.setPointsToWin(nbPointsToWin);
            } catch (NumberFormatException e) {
                final Context context = getApplicationContext();
                final String message = getString(R.string.enterANumberOfPointsToWin);
                int duration = Toast.LENGTH_SHORT;
                final Toast toast = Toast.makeText(context, message, duration);
                toast.show();
                return;
            }
        }

        Intent intent = new Intent(SelectNumberPointsActivity.this, SelectStoryTellerActivity.class);
        intent.putExtra("Game", gameBean);
        SelectNumberPointsActivity.this.startActivity(intent);
    }

}

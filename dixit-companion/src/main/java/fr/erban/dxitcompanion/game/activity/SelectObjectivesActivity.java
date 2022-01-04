package fr.erban.dxitcompanion.game.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.GameBean;
import fr.erban.dxitcompanion.game.turn.activity.SelectStoryTellerActivity;

public class SelectObjectivesActivity extends AppCompatActivity {

    private final static String DEFAULT_POINTS_TO_WIN = "30";
    private final static int DEFAULT_TABLE_TURNS_TO_WIN = 2;
    private boolean pointsToWinToggled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_objectives);

        final EditText pointsToWin = findViewById(R.id.pointsToWin);
        final EditText turnsToWin = findViewById(R.id.turnsToWin);

        final GameBean gameBean = (GameBean) getIntent().getSerializableExtra("Game");
        pointsToWin.setText(DEFAULT_POINTS_TO_WIN);
        final int defaultNbTurns = DEFAULT_TABLE_TURNS_TO_WIN * gameBean.getPlayers().size();
        turnsToWin.setText(Integer.toString(defaultNbTurns));
        LinearLayout turnBasedLayout = findViewById(R.id.turnBasedLayout);
        LinearLayout pointBasedLayout = findViewById(R.id.pointBasedLayout);
        pointsToWinToggled = true;
        updateLayoutsVisibility(turnBasedLayout, pointBasedLayout);

        final ImageView switchObjective = findViewById(R.id.switchObjective);
        switchObjective.setOnClickListener(v -> {
            pointsToWinToggled = !pointsToWinToggled;
            updateLayoutsVisibility(turnBasedLayout, pointBasedLayout);
        });
    }

    private void updateLayoutsVisibility(LinearLayout turnBasedLayout, LinearLayout pointBasedLayout) {
        if (pointsToWinToggled) {
            turnBasedLayout.setVisibility(View.INVISIBLE);
            pointBasedLayout.setVisibility(View.VISIBLE);
        } else {
            turnBasedLayout.setVisibility(View.VISIBLE);
            pointBasedLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void continueToFirstTurn(View view) {

        final EditText pointsToWin = findViewById(R.id.pointsToWin);
        final EditText turnsToWin = findViewById(R.id.turnsToWin);

        final GameBean gameBean = (GameBean) getIntent().getSerializableExtra("Game");

        if (gameBean != null) {
            final String pointsEntered = pointsToWin.getText()
                    .toString();
            final String turnsEntered = turnsToWin.getText()
                    .toString();
            if (pointsToWinToggled) {
                try {
                    final int nbPointsToWin = Integer.parseInt(pointsEntered);
                    final int nbTurnsToWin = Integer.MAX_VALUE;
                    if (nbPointsToWin == 0) {
                        throw new NumberFormatException();
                    }
                    gameBean.setMaxTurns(nbTurnsToWin);
                    gameBean.setPointsToWin(nbPointsToWin);
                } catch (NumberFormatException e) {
                    final Context context = getApplicationContext();
                    final String message = getString(R.string.enterANumberOfPointsToWin);
                    int duration = Toast.LENGTH_SHORT;
                    final Toast toast = Toast.makeText(context, message, duration);
                    toast.show();
                    return;
                }
            } else
                try {
                    final int nbPointsToWin = Integer.MAX_VALUE;
                    final int nbTurnsToWin = Integer.parseInt(turnsEntered);
                    if (nbTurnsToWin == 0) {
                        throw new NumberFormatException();
                    }
                    gameBean.setMaxTurns(nbTurnsToWin);
                    gameBean.setPointsToWin(nbPointsToWin);
                } catch (NumberFormatException e) {
                    final Context context = getApplicationContext();
                    final String message = getString(R.string.enterANumberOfTurnsToWin);
                    int duration = Toast.LENGTH_SHORT;
                    final Toast toast = Toast.makeText(context, message, duration);
                    toast.show();
                    return;
                }
            }

        Intent intent = new Intent(SelectObjectivesActivity.this, SelectStoryTellerActivity.class);
        intent.putExtra("Game", gameBean);
        SelectObjectivesActivity.this.startActivity(intent);
    }

}

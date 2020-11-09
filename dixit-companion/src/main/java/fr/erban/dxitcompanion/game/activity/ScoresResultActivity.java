package fr.erban.dxitcompanion.game.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;
import java.util.Random;

import fr.erban.dxitcompanion.MainActivity;
import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.game.GameBean;
import fr.erban.dxitcompanion.game.player.PlayerBean;
import fr.erban.dxitcompanion.game.player.TurnScore;

public class ScoresResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores_graph);

        final GameBean gameBean = (GameBean) getIntent().getSerializableExtra("Game");

        if (gameBean != null) {

            int maxPoints = 0;
            for (PlayerBean player : gameBean.getPlayers()) {
                if (player.getCurrentScore() > maxPoints) {
                    maxPoints = player.getCurrentScore();
                }
            }
            Random rnd = new Random();
            GraphView graph = findViewById(R.id.graph);
            graph.setClickable(true);
            graph.getViewport().setScalable(true);
            graph.getViewport().setScrollable(true);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(maxPoints);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(gameBean.getCurrentTurn());
            graph.getLegendRenderer().setVisible(true);
            graph.getLegendRenderer().setFixedPosition(0, maxPoints);

            for (PlayerBean player : gameBean.getPlayers()) {
                List<TurnScore> scorePerTurn = player.getScoresheet();
                LineGraphSeries<DataPoint> playerSeries = new LineGraphSeries<>();
                final DataPoint initialDp = new DataPoint(0, 0);
                playerSeries.appendData(initialDp, true, gameBean.getPointsToWin());
                for (TurnScore turnScore : scorePerTurn) {
                    DataPoint dataPoint = new DataPoint(turnScore.getTurn(), turnScore.getScore());
                    playerSeries.appendData(dataPoint, true, gameBean.getPointsToWin());
                }
                playerSeries.setAnimated(true);
                int color = Color.argb(255,
                        rnd.nextInt(256),
                        rnd.nextInt(256),
                        rnd.nextInt(256));
                playerSeries.setColor(color);
                playerSeries.setTitle(player.getName());
                playerSeries.setThickness(8);
                graph.addSeries(playerSeries);
            }
        }
    }

    public void continueToHomescreen(View view) {

        Intent intent = new Intent(ScoresResultActivity.this, MainActivity.class);
        ScoresResultActivity.this.startActivity(intent);
    }
}

package fr.erban.dxitcompanion.stats.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.db.player.PlayerEntity;
import fr.erban.dxitcompanion.db.player.PlayerViewModel;

public class StatsActivity extends AppCompatActivity {

    private PlayerViewModel playerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        final TableLayout playerStatsTable = findViewById(R.id.playerStatsTable);
        addPlayerStatsToTable(playerStatsTable);
    }

    private void addPlayerStatsToTable(TableLayout table) {

        final Observer<List<PlayerEntity>> playersInDbObserver = playersInDb -> {
            // Update the UI, in this case, a TextView.
            for (int i = 0, playerEntitiesSize = playersInDb.size(); i < playerEntitiesSize; i++) {
                PlayerEntity player = playersInDb.get(i);

                final TableRow playerRow = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);

                playerRow.setLayoutParams(lp);

                // Player name column
                TextView playerNameColumn = new TextView(this);
                playerNameColumn.setText(player.getName());
                playerNameColumn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                playerNameColumn.setGravity(Gravity.CENTER);
                playerRow.addView(playerNameColumn);

                // NumberOfGames column
                TextView numberOfGamesColumn = new TextView(this);
                numberOfGamesColumn.setText(String.valueOf(player.getNbGames()));
                numberOfGamesColumn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                numberOfGamesColumn.setGravity(Gravity.CENTER);
                playerRow.addView(numberOfGamesColumn);

                // NumberOfWins column
                TextView numberOfWinsColumn = new TextView(this);
                numberOfWinsColumn.setText(String.valueOf(player.getNbWins()));
                numberOfWinsColumn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                numberOfWinsColumn.setGravity(Gravity.CENTER);
                playerRow.addView(numberOfWinsColumn);

                // Win PErcentage column
                TextView winPercentageColumn = new TextView(this);
                winPercentageColumn.setText(player.getNbGames() != 0 ? String.valueOf(100 * player.getNbWins() / player.getNbGames()) : String.valueOf(0));
                winPercentageColumn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                winPercentageColumn.setGravity(Gravity.CENTER);
                playerRow.addView(winPercentageColumn);

                table.addView(playerRow, i + 1);
            }
        };

        playerViewModel.getPlayers().observe(this, playersInDbObserver);
    }

    private void sortPlayersByHeaderIdForNumbers(int headerId) {
        final TableLayout playerStatsTable = findViewById(R.id.playerStatsTable);

        List<TableRow> rows = new ArrayList<>();
        for (int i = 1; i < playerStatsTable.getChildCount(); i++) {
            rows.add((TableRow) playerStatsTable.getChildAt(i));
        }
        Collections.sort(rows, (o1, o2) -> {
            TextView t1 = (TextView) o1.getChildAt(headerId);
            double double1 = Double.parseDouble(t1.getText().toString());
            TextView t2 = (TextView) o2.getChildAt(headerId);
            double double2 = Double.parseDouble(t2.getText().toString());
            return Double.compare(double2, double1);
        });
        // remove all rows except the headers
        playerStatsTable.removeViews(1, playerStatsTable.getChildCount() - 1);

        for (int i = 0; i < rows.size(); i++) {
            TableRow row = rows.get(i);
            playerStatsTable.addView(row, i + 1);
        }
    }

    public void sortNumbers(View view) {

        int indexOfMyView = ((TableRow) view.getParent()).indexOfChild(view);
        sortPlayersByHeaderIdForNumbers(indexOfMyView);
    }

    public void sortNames(View view) {
        int indexOfMyView = ((TableRow) view.getParent()).indexOfChild(view);

        final TableLayout playerStatsTable = findViewById(R.id.playerStatsTable);

        List<TableRow> rows = new ArrayList<>();
        for (int i = 1; i < playerStatsTable.getChildCount(); i++) {
            rows.add((TableRow) playerStatsTable.getChildAt(i));
        }
        Collections.sort(rows, (o1, o2) -> {
            TextView t1 = (TextView) o1.getChildAt(indexOfMyView);
            String text1 = t1.getText().toString();
            TextView t2 = (TextView) o2.getChildAt(indexOfMyView);
            String text2 = t2.getText().toString();
            return text1.compareTo(text2);
        });
        // remove all rows except the headers
        playerStatsTable.removeViews(1, playerStatsTable.getChildCount() - 1);

        for (int i = 0; i < rows.size(); i++) {
            TableRow row = rows.get(i);
            playerStatsTable.addView(row, i + 1);
        }
    }
}

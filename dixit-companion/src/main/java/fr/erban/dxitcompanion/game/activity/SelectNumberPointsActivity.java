package fr.erban.dxitcompanion.game.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import fr.erban.dxitcompanion.R;
import fr.erban.dxitcompanion.db.CollectionsEnum;
import fr.erban.dxitcompanion.game.Game;
import fr.erban.dxitcompanion.game.turn.activity.SelectStoryTellerActivity;

import static android.content.ContentValues.TAG;

public class SelectNumberPointsActivity extends Activity {

    private final static String DEFAULT_POINTS_TO_WIN = "30";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_points);

        final EditText pointsToWin = findViewById(R.id.pointsToWin);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        pointsToWin.setText(DEFAULT_POINTS_TO_WIN);
    }

    public void continueToFirstTurn(View view) {

        final EditText pointsToWin = findViewById(R.id.pointsToWin);

        final Game game = (Game) getIntent().getSerializableExtra("Game");

        if (game != null) {
            game.setPointsToWin(Integer.parseInt(pointsToWin.getText()
                    .toString()));
        }

        if (game != null) {
            saveGameInDatabase(game);
        }

        Intent intent = new Intent(SelectNumberPointsActivity.this, SelectStoryTellerActivity.class);
        intent.putExtra("Game", game);
        SelectNumberPointsActivity.this.startActivity(intent);
    }

    private void saveGameInDatabase(Game game) {

        final Map<String, Object> gameToSave = new HashMap<>();
        gameToSave.put("players", game.getPlayers());
        gameToSave.put("pointsToWin", game.getPointsToWin());
        gameToSave.put("currentTurn", game.getCurrentTurn());
        gameToSave.put("isFinished", false);
        gameToSave.put("creator", game.getCreator());
        gameToSave.put("uuid", game.getUuid());

        db.collection(CollectionsEnum.GAMES.toString().toLowerCase())
                .add(gameToSave)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Game added with ID: " + documentReference.getId());
                    final SharedPreferences sharedPreferences = getSharedPreferences("DixitCompanionSettings", Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("currentGameId", documentReference.getId());
                    editor.apply();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding game", e));
    }

}

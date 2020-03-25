package fr.erban.dxitcompanion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;
import java.util.UUID;

import fr.erban.dxitcompanion.db.CollectionsEnum;
import fr.erban.dxitcompanion.game.Creator;
import fr.erban.dxitcompanion.game.activity.SelectPlayersActivity;

public class MainActivity extends Activity {

    private Creator creator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        retrieveOrCreateUserId();
        setContentView(R.layout.homescreen);
    }

    private void retrieveOrCreateUserId() {

        // Access the shared preferences on the phone
        final SharedPreferences sharedPreferences = getSharedPreferences("DixitCompanionSettings", Context.MODE_PRIVATE);

        String userId;
        if (!sharedPreferences.contains("userId")) {

            userId = UUID.randomUUID().toString();
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userId", userId);
            editor.apply();
        } else {
            userId = sharedPreferences.getString("userId", "default");
        }

        CollectionReference creator = FirebaseFirestore.getInstance().collection(CollectionsEnum.CREATORS.toString().toLowerCase());
        Query query = creator.whereEqualTo("userId", userId);
        if (query.get().isSuccessful()) {
            if (query.get().getResult() == null || Objects.requireNonNull(query.get().getResult()).isEmpty()) {
                this.creator = Creator.builder().userId(userId).build();
                creator.add(this.creator);
            }
        }
    }

    public void startNewGame(View view) {

        // lance une nouvelle partie
        Intent intent = new Intent(MainActivity.this, SelectPlayersActivity.class);
        intent.putExtra("Creator", creator);
        MainActivity.this.startActivity(intent);
    }
}

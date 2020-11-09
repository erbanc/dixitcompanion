package fr.erban.dxitcompanion.db.game;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.installations.FirebaseInstallations;

import java.util.Calendar;
import java.util.Date;

import fr.erban.dxitcompanion.db.DxitDatabase;
import fr.erban.dxitcompanion.db.enums.FirebaseReferencesEnum;
import fr.erban.dxitcompanion.game.GameBean;

/**
 * The Game Repository, it handles all databases at the same time
 */
class GameRepository {

    private final GameDao gameDao;
    private final FirebaseDatabase firebaseDatabase;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    GameRepository(Application application) {
        DxitDatabase db = DxitDatabase.getDatabase(application);
        gameDao = db.getGameDao();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(GameBean game) {
        DxitDatabase.databaseWriteExecutor.execute(() -> gameDao.insert(GameConverter.toEntity(game)));
        try {
            final Date currentTime = Calendar.getInstance().getTime();
            final String timeToString = currentTime.toString();
            FirebaseInstallations.getInstance().getId().addOnCompleteListener(task -> {

                if (task.getResult() != null) {
                    DatabaseReference gamesReference = firebaseDatabase
                            .getReference(task.getResult())
                            .child(FirebaseReferencesEnum.GAMES.name());
                    DatabaseReference child = gamesReference.child(timeToString);
                    child.setValue(game);
                }
            });
        } catch (Exception e) {
            Log.w("Firebase Error", "Error during saving game");
        }
    }
}

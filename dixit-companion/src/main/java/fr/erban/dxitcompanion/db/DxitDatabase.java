package fr.erban.dxitcompanion.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.erban.dxitcompanion.common.DxitConstants;
import fr.erban.dxitcompanion.db.crossref.GamePlayerCrossRefEntity;
import fr.erban.dxitcompanion.db.game.GameDao;
import fr.erban.dxitcompanion.db.game.GameEntity;
import fr.erban.dxitcompanion.db.player.PlayerDao;
import fr.erban.dxitcompanion.db.player.PlayerEntity;

@Database(version = 1, entities = {GameEntity.class, PlayerEntity.class, GamePlayerCrossRefEntity.class})
public abstract class DxitDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile DxitDatabase INSTANCE;

    public static DxitDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DxitDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DxitDatabase.class, DxitConstants.DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract GameDao getGameDao();

    public abstract PlayerDao getPlayerDao();

}

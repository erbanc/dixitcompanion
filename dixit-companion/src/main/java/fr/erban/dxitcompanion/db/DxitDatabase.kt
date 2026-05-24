package fr.erban.dxitcompanion.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import fr.erban.dxitcompanion.common.DxitConstants
import fr.erban.dxitcompanion.db.crossref.GamePlayerCrossRefEntity
import fr.erban.dxitcompanion.db.game.GameDao
import fr.erban.dxitcompanion.db.game.GameEntity
import fr.erban.dxitcompanion.db.player.PlayerDao
import fr.erban.dxitcompanion.db.player.PlayerEntity

@Database(
    version = 3,
    entities = [GameEntity::class, PlayerEntity::class, GamePlayerCrossRefEntity::class],
    exportSchema = true
)
abstract class DxitDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao
    abstract fun playerDao(): PlayerDao

    companion object {
        @Volatile
        private var INSTANCE: DxitDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // No schema change between v1 and v2
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE PlayerEntity ADD COLUMN colorHex TEXT NOT NULL DEFAULT '#5C3A9E'")
                db.execSQL("ALTER TABLE PlayerEntity ADD COLUMN emoji TEXT NOT NULL DEFAULT '🎭'")
                db.execSQL("ALTER TABLE GameEntity ADD COLUMN startedAt INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE GameEntity ADD COLUMN endedAt INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): DxitDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    DxitDatabase::class.java,
                    DxitConstants.DB_NAME
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}

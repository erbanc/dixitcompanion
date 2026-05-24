package fr.erban.dxitcompanion.db.game

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.erban.dxitcompanion.db.crossref.GamePlayerCrossRefEntity

@Dao
interface GameDao {

    @Insert
    suspend fun insert(game: GameEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReference(crossRef: GamePlayerCrossRefEntity)

    @Query("SELECT * FROM GameEntity WHERE finished = 1 ORDER BY endedAt DESC, idGame DESC")
    fun getAllFinished(): LiveData<List<GameEntity>>
}

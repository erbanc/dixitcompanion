package fr.erban.dxitcompanion.db.player

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PlayerDao {

    @Query("SELECT * FROM PlayerEntity")
    fun getAll(): LiveData<List<PlayerEntity>>

    @Insert
    suspend fun insert(player: PlayerEntity)

    @Upsert
    suspend fun upsert(players: List<PlayerEntity>)
}

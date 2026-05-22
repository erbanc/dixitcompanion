package fr.erban.dxitcompanion.db.player

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlayerDao {

    @Query("SELECT * FROM PlayerEntity")
    fun getAll(): LiveData<List<PlayerEntity>>

    @Insert
    suspend fun insert(player: PlayerEntity)

    @Update
    suspend fun update(players: List<PlayerEntity>)
}

package fr.erban.dxitcompanion.db.player;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {

    @Query("SELECT * FROM playerentity")
    LiveData<List<PlayerEntity>> getAll();

    @Query("SELECT * FROM playerentity WHERE name = :name")
    LiveData<PlayerEntity> getFromName(String name);

    @Insert
    void insert(PlayerEntity player);

    @Update
    void update(List<PlayerEntity> playersToUpdate);
}

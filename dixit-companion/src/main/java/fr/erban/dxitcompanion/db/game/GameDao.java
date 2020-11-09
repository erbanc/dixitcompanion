package fr.erban.dxitcompanion.db.game;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;

import fr.erban.dxitcompanion.db.crossref.GamePlayerCrossRefEntity;

@Dao
public interface GameDao {

    @Insert
    void insert(GameEntity gameEntity);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertReference(GamePlayerCrossRefEntity gamePlayerCrossRefEntity);
}


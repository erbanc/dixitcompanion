package fr.erban.dxitcompanion.db.crossref;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(primaryKeys = {"idGame", "name"})
public class GamePlayerCrossRefEntity {

    int idGame;

    @NonNull
    String name;
}

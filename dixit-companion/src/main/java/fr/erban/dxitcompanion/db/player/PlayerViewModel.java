package fr.erban.dxitcompanion.db.player;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private final LiveData<List<PlayerEntity>> players;
    private final PlayerRepository playerRepository;

    public PlayerViewModel(Application application) {
        super(application);
        playerRepository = new PlayerRepository(application);
        players = playerRepository.getAllPlayers();
    }

    public LiveData<List<PlayerEntity>> getPlayers() {
        return players;
    }

    public void insert(PlayerEntity player) {
        playerRepository.insert(player);
    }

    public void update(List<PlayerEntity> playersToUpdate) {
        playerRepository.update(playersToUpdate);
    }
}


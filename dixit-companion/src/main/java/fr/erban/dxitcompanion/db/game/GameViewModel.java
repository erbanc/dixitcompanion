package fr.erban.dxitcompanion.db.game;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import fr.erban.dxitcompanion.game.GameBean;

public class GameViewModel extends AndroidViewModel {

    private final GameRepository gameRepository;

    public GameViewModel(Application application) {
        super(application);
        gameRepository = new GameRepository(application);
    }

    public void insert(GameBean game) {
        gameRepository.insert(game);
    }
}


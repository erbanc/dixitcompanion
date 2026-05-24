package fr.erban.dxitcompanion.db.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import fr.erban.dxitcompanion.db.DxitDatabase
import fr.erban.dxitcompanion.game.GameBean
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val gameRepository = GameRepository(application)
    val finishedGames: LiveData<List<GameEntity>> =
        DxitDatabase.getDatabase(application).gameDao().getAllFinished()

    fun insert(game: GameBean) {
        viewModelScope.launch {
            gameRepository.insert(game)
        }
    }
}

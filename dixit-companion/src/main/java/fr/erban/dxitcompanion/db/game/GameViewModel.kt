package fr.erban.dxitcompanion.db.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.erban.dxitcompanion.game.GameBean
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val gameRepository = GameRepository(application)

    fun insert(game: GameBean) {
        viewModelScope.launch {
            gameRepository.insert(game)
        }
    }
}

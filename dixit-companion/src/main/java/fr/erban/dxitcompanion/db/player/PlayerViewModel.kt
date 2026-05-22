package fr.erban.dxitcompanion.db.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val playerRepository = PlayerRepository(application)
    val players: LiveData<List<PlayerEntity>> = playerRepository.allPlayers

    fun insert(player: PlayerEntity) {
        viewModelScope.launch {
            playerRepository.insert(player)
        }
    }

    fun update(players: List<PlayerEntity>) {
        viewModelScope.launch {
            playerRepository.update(players)
        }
    }
}

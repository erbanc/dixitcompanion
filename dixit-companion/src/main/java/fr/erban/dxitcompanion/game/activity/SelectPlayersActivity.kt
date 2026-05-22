package fr.erban.dxitcompanion.game.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import fr.erban.dxitcompanion.R
import fr.erban.dxitcompanion.databinding.SelectPlayersBinding
import fr.erban.dxitcompanion.db.player.PlayerConverter
import fr.erban.dxitcompanion.db.player.PlayerEntity
import fr.erban.dxitcompanion.db.player.PlayerViewModel
import fr.erban.dxitcompanion.game.GameBean
import fr.erban.dxitcompanion.game.player.PlayerBean

class SelectPlayersActivity : AppCompatActivity() {

    private lateinit var binding: SelectPlayersBinding
    private lateinit var playerViewModel: PlayerViewModel
    private var playersAlreadyInDb: List<String> = emptyList()
    private var playerEntities: List<PlayerEntity> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        super.onCreate(savedInstanceState)
        binding = SelectPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addPlayerButton.isEnabled = false

        binding.playerName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (s.toString().trim().isEmpty()) binding.addPlayerButton.isEnabled = false
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isEmpty()) binding.addPlayerButton.isEnabled = false
            }
            override fun afterTextChanged(s: Editable) = afterTextChangedVerify(s)
        })

        observePlayers()
    }

    private fun observePlayers() {
        playerViewModel.players.observe(this) { playersInDb ->
            playerEntities = playersInDb
            playersAlreadyInDb = playersInDb.map { it.name }
            if (playersInDb.isNotEmpty()) {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_dropdown_item_1line,
                    playersInDb.map { it.name }.toTypedArray()
                )
                binding.playerName.setAdapter(adapter)
            }
        }
    }

    private fun afterTextChangedVerify(s: Editable) {
        val input = s.toString().trim()
        val isDuplicate = (0 until binding.addedPlayers.childCount).any { i ->
            (binding.addedPlayers.getChildAt(i) as Chip).text.toString().equals(input, ignoreCase = true)
        }
        binding.addPlayerButton.isEnabled = input.isNotEmpty() && !isDuplicate
    }

    fun onAddPlayer(view: android.view.View) {
        val name = binding.playerName.text.toString().trim()
        if (name.isEmpty()) return

        val chip = Chip(this).apply {
            text = name
            isCloseIconVisible = true
            setTextAppearanceResource(R.style.chipText)
            isCheckable = false
            isChecked = true
            setOnCloseIconClickListener { binding.addedPlayers.removeView(this) }
            isHapticFeedbackEnabled = true
        }
        binding.addedPlayers.addView(chip)
        binding.playerName.setText("")
    }

    fun continueToNumberPoints(view: android.view.View) {
        val players = retrievePlayersFromChips()
        if (players.size < 3) {
            Toast.makeText(this, getString(R.string.notEnoughPlayers), Toast.LENGTH_SHORT).show()
            return
        }
        createNewPlayersInDatabase(players)
        val gameBean = GameBean(players = players)
        startActivity(Intent(this, SelectObjectivesActivity::class.java).putExtra("Game", gameBean))
    }

    private fun createNewPlayersInDatabase(players: List<PlayerBean>) {
        players.filter { !it.persisted }.forEach { player ->
            playerViewModel.insert(PlayerConverter.toEntity(player))
        }
    }

    private fun retrievePlayersFromChips(): List<PlayerBean> =
        (0 until binding.addedPlayers.childCount).map { i ->
            val name = (binding.addedPlayers.getChildAt(i) as Chip).text.toString()
            if (playersAlreadyInDb.contains(name)) {
                PlayerConverter.toBean(playerEntities.first { it.name == name })
            } else {
                PlayerBean(name = name, persisted = false)
            }
        }
}

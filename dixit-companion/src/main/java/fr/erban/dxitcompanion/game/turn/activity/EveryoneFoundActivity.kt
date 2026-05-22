package fr.erban.dxitcompanion.game.turn.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import fr.erban.dxitcompanion.R
import fr.erban.dxitcompanion.common.parcelableExtra
import fr.erban.dxitcompanion.databinding.EveryoneFoundBinding
import fr.erban.dxitcompanion.game.GameBean
import fr.erban.dxitcompanion.game.turn.Turn

class EveryoneFoundActivity : AppCompatActivity() {

    private lateinit var binding: EveryoneFoundBinding
    private lateinit var gameBean: GameBean
    private lateinit var turn: Turn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EveryoneFoundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        turn = intent.parcelableExtra("Turn")!!
        gameBean = intent.parcelableExtra("Game")!!
        binding.turnNumber.text = getString(R.string.turnNumberPrefix) + gameBean.currentTurn
    }

    fun clickYes(view: View) {
        turn = turn.copy(everybodyFound = true)
        startActivity(
            Intent(this, EndTurnActivity::class.java)
                .putExtra("Game", gameBean)
                .putExtra("Turn", turn)
        )
    }

    fun clickNo(view: View) {
        turn = turn.copy(everybodyFound = false)
        startActivity(
            Intent(this, WhoDidFindActivity::class.java)
                .putExtra("Game", gameBean)
                .putExtra("Turn", turn)
        )
    }
}

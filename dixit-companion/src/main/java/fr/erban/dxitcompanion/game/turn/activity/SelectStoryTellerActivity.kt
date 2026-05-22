package fr.erban.dxitcompanion.game.turn.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import fr.erban.dxitcompanion.R
import fr.erban.dxitcompanion.common.parcelableExtra
import fr.erban.dxitcompanion.databinding.SelectStorytellerBinding
import fr.erban.dxitcompanion.game.GameBean
import fr.erban.dxitcompanion.game.turn.Turn

class SelectStoryTellerActivity : AppCompatActivity() {

    private lateinit var binding: SelectStorytellerBinding
    private lateinit var gameBean: GameBean
    private var turn = Turn()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SelectStorytellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameBean = intent.parcelableExtra<GameBean>("Game")!!
            .copy(currentTurn = intent.parcelableExtra<GameBean>("Game")!!.currentTurn + 1)

        binding.turnNumber.text = getString(R.string.turnNumberPrefix) + gameBean.currentTurn

        gameBean.players.forEachIndexed { index, player ->
            val radioButton = RadioButton(this).apply {
                text = player.name
                visibility = View.VISIBLE
                textSize = 50f
                typeface = ResourcesCompat.getFont(this@SelectStoryTellerActivity, R.font.write_me_a_song)
                setTextColor(resources.getColor(R.color.backgroundTextColor, theme))
            }
            binding.radioGroupChooseStoryteller.addView(radioButton)
            if (index == 0) radioButton.isChecked = true
        }
    }

    fun continueToTurnResults(view: View) {
        val selectedId = binding.radioGroupChooseStoryteller.checkedRadioButtonId
        val rb = binding.radioGroupChooseStoryteller.findViewById<RadioButton>(selectedId)
        val storyTellerName = rb.text.toString()

        val storyTeller = gameBean.players.first { it.name == storyTellerName }
        turn = turn.copy(storyTeller = storyTeller)

        startActivity(
            Intent(this, EveryoneFoundActivity::class.java)
                .putExtra("Game", gameBean)
                .putExtra("Turn", turn)
        )
    }
}

package fr.erban.dxitcompanion

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import fr.erban.dxitcompanion.databinding.HomescreenBinding
import fr.erban.dxitcompanion.game.activity.SelectPlayersActivity
import fr.erban.dxitcompanion.rules.activity.RulesActivity
import fr.erban.dxitcompanion.stats.activity.StatsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: HomescreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomescreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun startNewGame(view: View) {
        startActivity(Intent(this, SelectPlayersActivity::class.java))
    }

    fun goToStats(view: View) {
        startActivity(Intent(this, StatsActivity::class.java))
    }

    fun goToRules(view: View) {
        startActivity(Intent(this, RulesActivity::class.java))
    }
}

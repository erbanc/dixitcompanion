package fr.erban.dxitcompanion.rules.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.erban.dxitcompanion.databinding.RulesBinding

class RulesActivity : AppCompatActivity() {

    private lateinit var binding: RulesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RulesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

package fr.erban.dixitcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import fr.erban.dixitcompanion.db.game.GameViewModel
import fr.erban.dixitcompanion.db.player.PlayerViewModel
import fr.erban.dixitcompanion.ui.navigation.DixitNavGraph
import fr.erban.dixitcompanion.ui.theme.DixitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        val gameViewModel   = ViewModelProvider(this)[GameViewModel::class.java]
        setContent {
            DixitTheme {
                val navController = rememberNavController()
                DixitNavGraph(navController, playerViewModel, gameViewModel)
            }
        }
    }
}

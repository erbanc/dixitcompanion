package fr.erban.dxitcompanion.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.erban.dxitcompanion.R
import fr.erban.dxitcompanion.ui.components.DixitScaffold

@Composable
fun RulesScreen() {
    DixitScaffold(title = "Comment jouer") {
        Card(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                stringResource(R.string.rules),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
            )
        }
    }
}

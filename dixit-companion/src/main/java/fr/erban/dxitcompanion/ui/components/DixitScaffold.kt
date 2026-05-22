package fr.erban.dxitcompanion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.erban.dxitcompanion.ui.theme.DeepNight
import fr.erban.dxitcompanion.ui.theme.Purple
import fr.erban.dxitcompanion.ui.theme.PurpleContainer

@Composable
fun DixitScaffold(
    title: String,
    turnNumber: Int? = null,
    subtitle: String? = null,
    titleLines: Int = 1,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Turn badge
        if (turnNumber != null) {
            Surface(
                shape = RoundedCornerShape(50),
                color = PurpleContainer,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    "TOUR $turnNumber",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Purple
                )
            }
        }
        Column(Modifier.fillMaxSize()) {
            val topPad = if (turnNumber != null) 52.dp else 40.dp
            Text(
                title,
                style = if (title.length > 12) MaterialTheme.typography.headlineMedium
                        else MaterialTheme.typography.headlineLarge,
                color = DeepNight,
                textAlign = TextAlign.Center,
                maxLines = titleLines,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = topPad, bottom = 8.dp, start = 16.dp, end = 16.dp)
            )
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Purple,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            Box(Modifier.fillMaxSize()) { content() }
        }
    }
}

@Composable
fun BottomCTA(label: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Purple,
            contentColor = Color.White,
            disabledContainerColor = PurpleContainer,
            disabledContentColor = Purple
        )
    ) {
        Text(label, style = MaterialTheme.typography.titleLarge)
    }
}

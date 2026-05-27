package fr.erban.dixitcompanion.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayerAvatar(
    emoji: String,
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    border: BorderStroke? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.18f))
            .then(
                if (border != null) Modifier.border(border, CircleShape)
                else Modifier.border(2.dp, color, CircleShape)
            ),
        contentAlignment = Alignment.Center
    ) {
        val emojiSize = (size.value * 0.55f).sp
        Text(
            text = emoji,
            style = TextStyle(fontSize = emojiSize)
        )
    }
}

@Composable
fun PlayerInitial(
    name: String,
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.firstOrNull()?.uppercase() ?: "?",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}

package fr.erban.dixitcompanion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.erban.dixitcompanion.ui.components.DixitScaffold

@Composable
fun RulesScreen() {
    DixitScaffold(title = "Comment jouer") {
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AutoStories,
                            null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "Le jeu d'imagination où les mots ouvrent des portes vers d'autres mondes.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            item {
                RuleSection(emoji = "🎯", title = "But du jeu") {
                    RuleText("Être le premier joueur à atteindre le score fixé au début de la partie.")
                }
            }

            item {
                RuleSection(emoji = "🃏", title = "Matériel") {
                    RuleText("Des cartes Dixit (au moins 84 cartes) et 3 à 6 joueurs.")
                }
            }

            item {
                RuleSection(emoji = "🎮", title = "Déroulement d'un tour") {
                    RuleStep(1, "Distribuez 5 cartes à chaque joueur.")
                    RuleStep(2, "Le conteur choisit une carte et énonce un mot ou une phrase.")
                    RuleStep(3, "Chaque autre joueur choisit une de ses cartes qui correspond.")
                    RuleStep(4, "Le conteur mélange toutes les cartes et les étale sur la table.")
                    RuleStep(5, "Les joueurs identifient la carte du conteur sans se consulter.")
                    RuleStep(6, "Décompte 3, 2, 1 — chaque joueur pointe la carte de son choix.")
                    RuleStep(7, "Entrez les choix dans l'app — les scores sont calculés automatiquement.")
                    RuleStep(8, "Tout le monde pioche une carte. Le joueur à gauche devient le nouveau conteur.")
                }
            }

            item {
                RuleSection(emoji = "📊", title = "Calcul des points") {
                    ScoreCard(
                        condition = "Personne ou tout le monde trouve la carte",
                        result = "Conteur : 0 pt",
                        result2 = "Autres joueurs : +2 pts chacun"
                    )
                    Spacer(Modifier.height(8.dp))
                    ScoreCard(
                        condition = "Au moins une personne trouve la carte",
                        result = "Conteur : +3 pts",
                        result2 = "Trouveurs : +3 pts chacun"
                    )
                    Spacer(Modifier.height(8.dp))
                    ScoreCard(
                        condition = "Votes reçus sur sa propre carte (hors conteur)",
                        result = "+1 pt par vote reçu"
                    )
                }
            }

            item {
                RuleSection(emoji = "🏆", title = "Victoire") {
                    RuleText("Le gagnant est le premier à atteindre le score fixé. En cas d'égalité au moment où le seuil est franchi, la partie continue jusqu'à départager.")
                }
            }

            item {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Merci d'avoir téléchargé l'application ✦ Toute suggestion est la bienvenue sur le Play Store.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun RuleSection(
    emoji: String,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(emoji, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.width(8.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun RuleText(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun RuleStep(number: Int, text: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    "$number",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ScoreCard(
    condition: String,
    result: String,
    result2: String? = null
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            condition,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        Text(
            result,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        if (result2 != null) {
            Text(
                result2,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

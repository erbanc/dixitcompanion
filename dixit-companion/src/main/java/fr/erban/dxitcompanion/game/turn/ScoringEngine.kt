package fr.erban.dxitcompanion.game.turn

import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.game.player.TurnScore
import fr.erban.dxitcompanion.game.turn.bean.VoteBean

object ScoringEngine {

    fun computeScore(player: PlayerBean, turn: Turn, currentTurn: Int): PlayerBean {
        val storyteller = turn.storyTeller!!
        val votes = turn.votes

        // Derive the effective outcome from actual votes — overrides the UI flags
        // when the votes contradict what was declared at the "who found" step.
        val nonStorytellerVotes = votes.filter { it.voter.name != storyteller.name }
        val allVotedForStoryteller = nonStorytellerVotes.isNotEmpty() &&
            nonStorytellerVotes.all { it.elected.name == storyteller.name }
        val noneVotedForStoryteller = nonStorytellerVotes.isNotEmpty() &&
            nonStorytellerVotes.none { it.elected.name == storyteller.name }

        val effectivelyEverybodyFound = turn.everybodyFound || allVotedForStoryteller
        val effectivelyNoOneFound    = turn.noOneFound    || noneVotedForStoryteller

        val votesForCard  = votes.count { it.elected.name == player.name }
        val hasFoundCard  = votes.any   { it.voter.name == player.name && it.elected.name == storyteller.name }

        val lastTurnScore = when {
            player.name == storyteller.name -> if (effectivelyNoOneFound || effectivelyEverybodyFound) 0 else 3
            effectivelyNoOneFound || effectivelyEverybodyFound -> 2
            else -> (if (hasFoundCard) 3 else 0) + votesForCard
        }

        val isStoryteller          = player.name == storyteller.name
        val isVoter                = !isStoryteller
        // "found" = voted for storyteller OR the everyone-found shortcut was used
        val foundStoryteller       = isVoter && (hasFoundCard || effectivelyEverybodyFound)
        // Optimal storyteller turn = some found, not all and not none → storyteller scored +3
        val isStorytellerOptimal   = isStoryteller && !effectivelyNoOneFound && !effectivelyEverybodyFound

        return player.copy(
            currentScore                = player.currentScore + lastTurnScore,
            scoreLastTurn               = lastTurnScore,
            scoresheet                  = player.scoresheet + TurnScore(turn = currentTurn, score = player.currentScore + lastTurnScore),
            nbAsStoryteller             = player.nbAsStoryteller + if (isStoryteller) 1 else 0,
            nbAsVoter                   = player.nbAsVoter       + if (isVoter) 1 else 0,
            nbFoundStoryteller          = player.nbFoundStoryteller + if (foundStoryteller) 1 else 0,
            nbStorytellerOptimalTurns   = player.nbStorytellerOptimalTurns + if (isStorytellerOptimal) 1 else 0
        )
    }
}

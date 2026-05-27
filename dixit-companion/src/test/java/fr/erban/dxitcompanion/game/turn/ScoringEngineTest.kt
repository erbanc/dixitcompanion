package fr.erban.dxitcompanion.game.turn

import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.game.turn.bean.VoteBean
import org.junit.Assert.assertEquals
import org.junit.Test

class ScoringEngineTest {

    private val storyteller = PlayerBean(name = "Alice")
    private val player2    = PlayerBean(name = "Bob")
    private val player3    = PlayerBean(name = "Charlie")
    private val player4    = PlayerBean(name = "Dave")

    private val noOneFoundTurn = Turn(
        storyTeller = storyteller, noOneFound = true, everybodyFound = false, votes = emptyList()
    )
    private val everybodyFoundTurn = Turn(
        storyTeller = storyteller, noOneFound = false, everybodyFound = true, votes = emptyList()
    )

    // ── flags: noOneFound / everybodyFound ───────────────────────────────────

    @Test fun whenNobodyFound_storytellerGetsZero() =
        assertEquals(0, ScoringEngine.computeScore(storyteller, noOneFoundTurn, 1).scoreLastTurn)

    @Test fun whenNobodyFound_nonStorytellersGetTwo() =
        assertEquals(2, ScoringEngine.computeScore(player2, noOneFoundTurn, 1).scoreLastTurn)

    @Test fun whenEverybodyFound_storytellerGetsZero() =
        assertEquals(0, ScoringEngine.computeScore(storyteller, everybodyFoundTurn, 1).scoreLastTurn)

    @Test fun whenEverybodyFound_nonStorytellersGetTwo() =
        assertEquals(2, ScoringEngine.computeScore(player2, everybodyFoundTurn, 1).scoreLastTurn)

    // ── votes: some found ────────────────────────────────────────────────────

    @Test fun whenSomeFound_storytellerGetsThree() {
        val votes = listOf(
            VoteBean(voter = player2, elected = storyteller),
            VoteBean(voter = player3, elected = player2)
        )
        val turn = Turn(storyTeller = storyteller, votes = votes)
        assertEquals(3, ScoringEngine.computeScore(storyteller, turn, 1).scoreLastTurn)
    }

    @Test fun whenSomeFound_finderGetsPlusThreePlusVoteBonus() {
        val votes = listOf(
            VoteBean(voter = player2, elected = storyteller),
            VoteBean(voter = player3, elected = player2)        // +1 bonus for Bob
        )
        val turn = Turn(storyTeller = storyteller, votes = votes)
        assertEquals(4, ScoringEngine.computeScore(player2, turn, 1).scoreLastTurn)
    }

    @Test fun whenSomeFound_nonFinderGetsZero() {
        val votes = listOf(
            VoteBean(voter = player2, elected = storyteller),
            VoteBean(voter = player3, elected = player2)
        )
        val turn = Turn(storyTeller = storyteller, votes = votes)
        assertEquals(0, ScoringEngine.computeScore(player3, turn, 1).scoreLastTurn)
    }

    @Test fun voteBonus_twoVotesOnSameCard() {
        val votes = listOf(
            VoteBean(voter = player2, elected = storyteller),
            VoteBean(voter = player3, elected = player2),
            VoteBean(voter = player4, elected = player2)        // second vote for Bob
        )
        val turn = Turn(storyTeller = storyteller, votes = votes)
        // Bob found (+3) + 2 votes on his card (+2) = 5
        assertEquals(5, ScoringEngine.computeScore(player2, turn, 1).scoreLastTurn)
    }

    @Test fun voteBonus_notCountedWhenNobodyFound() {
        val votes = listOf(
            VoteBean(voter = player2, elected = player3),
            VoteBean(voter = player3, elected = player2)
        )
        val turn = Turn(storyTeller = storyteller, noOneFound = true, votes = votes)
        assertEquals(2, ScoringEngine.computeScore(player3, turn, 1).scoreLastTurn)
    }

    // ── edge case: all voted for storyteller via mixed path ─────────────────

    @Test fun allVotedForStoryteller_viaWhoFound_treatedAsEverybodyFound() {
        // Alice is storyteller; Bob explicitly "found" (auto-vote), Charlie also votes storyteller.
        // noOneFound=false, everybodyFound=false — but effectively everyone found.
        val votes = listOf(
            VoteBean(voter = player2, elected = storyteller),   // Bob — auto via whoFound
            VoteBean(voter = player3, elected = storyteller)    // Charlie — explicit vote
        )
        val turn = Turn(storyTeller = storyteller, noOneFound = false, everybodyFound = false, votes = votes)
        // Storyteller should get 0, not 3
        assertEquals(0, ScoringEngine.computeScore(storyteller, turn, 1).scoreLastTurn)
        // Non-storytellers should each get 2, not 3
        assertEquals(2, ScoringEngine.computeScore(player2, turn, 1).scoreLastTurn)
        assertEquals(2, ScoringEngine.computeScore(player3, turn, 1).scoreLastTurn)
    }

    @Test fun noOneVotedForStoryteller_derivedFromVotes_storytellerGetsZero() {
        // Nobody voted for storyteller (noOneFound flag not set explicitly)
        val votes = listOf(
            VoteBean(voter = player2, elected = player3),
            VoteBean(voter = player3, elected = player2)
        )
        val turn = Turn(storyTeller = storyteller, noOneFound = false, everybodyFound = false, votes = votes)
        assertEquals(0, ScoringEngine.computeScore(storyteller, turn, 1).scoreLastTurn)
        assertEquals(2, ScoringEngine.computeScore(player2, turn, 1).scoreLastTurn)
        assertEquals(2, ScoringEngine.computeScore(player3, turn, 1).scoreLastTurn)
    }

    @Test fun noOneVotedForStoryteller_derivedFromVotes_noVoteBonus() {
        // Everyone voted for each other, nobody for storyteller.
        // +2 flat, no vote bonus — Bob got a vote from Charlie but that doesn't matter.
        val votes = listOf(
            VoteBean(voter = player2, elected = player3),
            VoteBean(voter = player3, elected = player2)
        )
        val turn = Turn(storyTeller = storyteller, noOneFound = false, everybodyFound = false, votes = votes)
        assertEquals(2, ScoringEngine.computeScore(player2, turn, 1).scoreLastTurn)
    }

    // ── per-turn stat counters ────────────────────────────────────────────────

    @Test fun storyteller_incrementsNbAsStoryteller() {
        val result = ScoringEngine.computeScore(storyteller, noOneFoundTurn, 1)
        assertEquals(1, result.nbAsStoryteller)
        assertEquals(0, result.nbAsVoter)
        assertEquals(0, result.nbFoundStoryteller)
    }

    @Test fun voter_incrementsNbAsVoter() {
        val result = ScoringEngine.computeScore(player2, noOneFoundTurn, 1)
        assertEquals(0, result.nbAsStoryteller)
        assertEquals(1, result.nbAsVoter)
    }

    @Test fun voter_whoFoundStoryteller_incrementsNbFoundStoryteller() {
        val votes = listOf(
            VoteBean(voter = player2, elected = storyteller),
            VoteBean(voter = player3, elected = player2)
        )
        val turn = Turn(storyTeller = storyteller, votes = votes)
        val bob = ScoringEngine.computeScore(player2, turn, 1)
        assertEquals(1, bob.nbFoundStoryteller)
        val charlie = ScoringEngine.computeScore(player3, turn, 1)
        assertEquals(0, charlie.nbFoundStoryteller)
    }

    @Test fun voter_inEverybodyFoundShortcut_incrementsNbFoundStoryteller() {
        val result = ScoringEngine.computeScore(player2, everybodyFoundTurn, 1)
        assertEquals(1, result.nbFoundStoryteller)
    }

    // ── nbStorytellerOptimalTurns ─────────────────────────────────────────────

    @Test fun storyteller_someFound_incrementsOptimalTurns() {
        val votes = listOf(
            VoteBean(voter = player2, elected = storyteller),
            VoteBean(voter = player3, elected = player2)
        )
        val turn = Turn(storyTeller = storyteller, votes = votes)
        val result = ScoringEngine.computeScore(storyteller, turn, 1)
        assertEquals(1, result.nbStorytellerOptimalTurns)
    }

    @Test fun storyteller_everybodyFound_doesNotIncrementOptimalTurns() {
        val result = ScoringEngine.computeScore(storyteller, everybodyFoundTurn, 1)
        assertEquals(0, result.nbStorytellerOptimalTurns)
    }

    @Test fun storyteller_nobodyFound_doesNotIncrementOptimalTurns() {
        val result = ScoringEngine.computeScore(storyteller, noOneFoundTurn, 1)
        assertEquals(0, result.nbStorytellerOptimalTurns)
    }

    @Test fun storyteller_allVotedForStoryteller_derivedEverybodyFound_doesNotIncrementOptimalTurns() {
        val votes = listOf(
            VoteBean(voter = player2, elected = storyteller),
            VoteBean(voter = player3, elected = storyteller)
        )
        val turn = Turn(storyTeller = storyteller, votes = votes)
        val result = ScoringEngine.computeScore(storyteller, turn, 1)
        assertEquals(0, result.nbStorytellerOptimalTurns)
    }

    @Test fun voter_neverIncrements_nbStorytellerOptimalTurns() {
        val votes = listOf(
            VoteBean(voter = player2, elected = storyteller),
            VoteBean(voter = player3, elected = player2)
        )
        val turn = Turn(storyTeller = storyteller, votes = votes)
        val result = ScoringEngine.computeScore(player2, turn, 1)
        assertEquals(0, result.nbStorytellerOptimalTurns)
    }

    // ── score accumulation ────────────────────────────────────────────────────

    @Test fun scoreAccumulates_acrossMultipleTurns() {
        val turn1 = Turn(storyTeller = storyteller, noOneFound = true, votes = emptyList())
        val afterTurn1 = ScoringEngine.computeScore(player2, turn1, 1)
        assertEquals(2, afterTurn1.currentScore)

        // Turn 2: Bob conteur. Charlie trouve (vote Bob), Dave ne trouve pas → "certains ont trouvé"
        val votes2 = listOf(
            VoteBean(voter = player3, elected = afterTurn1),  // Charlie trouve Bob
            VoteBean(voter = player4, elected = player3)      // Dave vote Charlie, pas le conteur
        )
        val turn2 = Turn(storyTeller = afterTurn1, votes = votes2)
        val afterTurn2 = ScoringEngine.computeScore(afterTurn1, turn2, 2)
        // Bob conteur : +3 → 2+3=5
        assertEquals(5, afterTurn2.currentScore)
    }

    @Test fun scoresheetIsUpdatedWithCurrentTurn() {
        val turn = Turn(storyTeller = storyteller, noOneFound = true, votes = emptyList())
        val result = ScoringEngine.computeScore(player2, turn, 3)
        assertEquals(1, result.scoresheet.size)
        assertEquals(3, result.scoresheet[0].turn)
        assertEquals(2, result.scoresheet[0].score)
    }
}

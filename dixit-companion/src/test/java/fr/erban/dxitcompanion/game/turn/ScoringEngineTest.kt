package fr.erban.dxitcompanion.game.turn

import fr.erban.dxitcompanion.game.player.PlayerBean
import fr.erban.dxitcompanion.game.player.TurnScore
import fr.erban.dxitcompanion.game.turn.bean.VoteBean
import org.junit.Assert.assertEquals
import org.junit.Test

class ScoringEngineTest {

    private val storyteller = PlayerBean(name = "Alice")
    private val player2 = PlayerBean(name = "Bob")
    private val player3 = PlayerBean(name = "Charlie")

    // Turn where noOneFound = true
    private val noOneFoundTurn = Turn(
        storyTeller = storyteller,
        noOneFound = true,
        everybodyFound = false,
        votes = emptyList()
    )

    // Turn where everybodyFound = true
    private val everybodyFoundTurn = Turn(
        storyTeller = storyteller,
        noOneFound = false,
        everybodyFound = true,
        votes = emptyList()
    )

    @Test
    fun whenNobodyFound_storytellerGetsZero() {
        val result = ScoringEngine.computeScore(storyteller, noOneFoundTurn, currentTurn = 1)
        assertEquals(0, result.scoreLastTurn)
        assertEquals(0, result.currentScore)
    }

    @Test
    fun whenNobodyFound_nonStorytellersGetTwo() {
        val result = ScoringEngine.computeScore(player2, noOneFoundTurn, currentTurn = 1)
        assertEquals(2, result.scoreLastTurn)
        assertEquals(2, result.currentScore)
    }

    @Test
    fun whenEverybodyFound_storytellerGetsZero() {
        val result = ScoringEngine.computeScore(storyteller, everybodyFoundTurn, currentTurn = 1)
        assertEquals(0, result.scoreLastTurn)
        assertEquals(0, result.currentScore)
    }

    @Test
    fun whenEverybodyFound_nonStorytellersGetTwo() {
        val result = ScoringEngine.computeScore(player2, everybodyFoundTurn, currentTurn = 1)
        assertEquals(2, result.scoreLastTurn)
        assertEquals(2, result.currentScore)
    }

    @Test
    fun whenSomeFound_storytellerGetsThree() {
        // Bob found the storyteller's card, Charlie did not
        val votes = listOf(
            VoteBean(voter = player2, elected = storyteller),   // Bob found Alice's card
            VoteBean(voter = player3, elected = player2)        // Charlie voted for Bob's card
        )
        val someTurn = Turn(storyTeller = storyteller, noOneFound = false, everybodyFound = false, votes = votes)
        val result = ScoringEngine.computeScore(storyteller, someTurn, currentTurn = 1)
        assertEquals(3, result.scoreLastTurn)
        assertEquals(3, result.currentScore)
    }

    @Test
    fun whenSomeFound_finderGetsThree() {
        // Bob found the storyteller's card
        val votes = listOf(
            VoteBean(voter = player2, elected = storyteller),   // Bob found Alice's card
            VoteBean(voter = player3, elected = player2)        // Charlie voted for Bob's card
        )
        val someTurn = Turn(storyTeller = storyteller, noOneFound = false, everybodyFound = false, votes = votes)
        val result = ScoringEngine.computeScore(player2, someTurn, currentTurn = 1)
        // Bob found the card (+3) + Charlie voted for Bob's card (+1) = 4
        assertEquals(4, result.scoreLastTurn)
        assertEquals(4, result.currentScore)
    }

    @Test
    fun whenSomeFound_nonFinderGetsZero() {
        // Charlie did NOT find the storyteller's card
        val votes = listOf(
            VoteBean(voter = player2, elected = storyteller),   // Bob found Alice's card
            VoteBean(voter = player3, elected = player2)        // Charlie voted for Bob's card (did not find)
        )
        val someTurn = Turn(storyTeller = storyteller, noOneFound = false, everybodyFound = false, votes = votes)
        val result = ScoringEngine.computeScore(player3, someTurn, currentTurn = 1)
        // Charlie did not find (+0), nobody voted for Charlie's card (+0) = 0
        assertEquals(0, result.scoreLastTurn)
        assertEquals(0, result.currentScore)
    }

    @Test
    fun voteBonus_addsOnePerVoteReceivedOnOwnCard() {
        // Two players voted for Bob's card
        val votes = listOf(
            VoteBean(voter = player3, elected = player2),       // Charlie voted for Bob's card
            VoteBean(voter = player2, elected = storyteller)    // Bob found Alice's card (so Bob is a finder)
        )
        // Add another non-storyteller who also votes for Bob
        val player4 = PlayerBean(name = "Dave")
        val votesWithExtra = votes + VoteBean(voter = player4, elected = player2)  // Dave also voted for Bob
        val someTurn = Turn(storyTeller = storyteller, noOneFound = false, everybodyFound = false, votes = votesWithExtra)
        val result = ScoringEngine.computeScore(player2, someTurn, currentTurn = 1)
        // Bob found (+3) + 2 votes for Bob's card (+2) = 5
        assertEquals(5, result.scoreLastTurn)
        assertEquals(5, result.currentScore)
    }

    @Test
    fun voteBonus_notCountedWhenNobodyFound() {
        // In noOneFound scenario, votes are ignored
        val votes = listOf(
            VoteBean(voter = player2, elected = player3),
            VoteBean(voter = player3, elected = player2)
        )
        val noOneTurnWithVotes = Turn(
            storyTeller = storyteller,
            noOneFound = true,
            everybodyFound = false,
            votes = votes
        )
        // player3 received a vote from player2, but should still only get 2
        val result = ScoringEngine.computeScore(player3, noOneTurnWithVotes, currentTurn = 1)
        assertEquals(2, result.scoreLastTurn)
        assertEquals(2, result.currentScore)
    }

    @Test
    fun scoreAccumulates_acrossMultipleTurns() {
        // Turn 1: noOneFound -> Bob gets 2
        val turn1 = Turn(storyTeller = storyteller, noOneFound = true, everybodyFound = false, votes = emptyList())
        val afterTurn1 = ScoringEngine.computeScore(player2, turn1, currentTurn = 1)
        assertEquals(2, afterTurn1.currentScore)

        // Turn 2: Bob is storyteller, some found -> storyteller gets 3
        val votes2 = listOf(
            VoteBean(voter = player3, elected = player2)  // Charlie found Bob's card
        )
        val turn2 = Turn(storyTeller = afterTurn1, noOneFound = false, everybodyFound = false, votes = votes2)
        val afterTurn2 = ScoringEngine.computeScore(afterTurn1, turn2, currentTurn = 2)
        // Bob (storyteller): +3 => 2 + 3 = 5
        assertEquals(5, afterTurn2.currentScore)
    }

    @Test
    fun scoresheetIsUpdatedWithCurrentTurn() {
        val turn = Turn(storyTeller = storyteller, noOneFound = true, everybodyFound = false, votes = emptyList())
        val result = ScoringEngine.computeScore(player2, turn, currentTurn = 3)
        assertEquals(1, result.scoresheet.size)
        assertEquals(3, result.scoresheet[0].turn)
        assertEquals(2, result.scoresheet[0].score)
    }
}

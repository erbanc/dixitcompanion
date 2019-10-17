package fr.erban.dixitcompanion.game.turn;

import fr.erban.dixitcompanion.game.player.Player;

public class CountPointsService {

    public int getPointsForTheTurn(final Player player, final int numberOfVotes, final boolean hasFoundCard, final Turn turn) {

        int pointsGranted = 0;

        if (turn.getStoryTeller().equals(player)) {

            if (turn.isNoOneFound() || turn.isEverybodyFound()) {
                return 0;
            } else {
                return 3;
            }
        } else {
            if (turn.isNoOneFound() || turn.isEverybodyFound()) {
                pointsGranted += 2;
            } else if (hasFoundCard) {
                pointsGranted += 3;
            }

            pointsGranted += numberOfVotes;
        }

        return pointsGranted;
    }
}

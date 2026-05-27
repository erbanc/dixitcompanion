package fr.erban.dxitcompanion.db.player

import fr.erban.dxitcompanion.game.player.PlayerBean

object PlayerConverter {

    fun toBean(entity: PlayerEntity): PlayerBean = PlayerBean(
        name                        = entity.name,
        currentScore                = 0,
        nbGames                     = entity.nbGames,
        nbWins                      = entity.nbWins,
        nbAsStoryteller             = entity.nbAsStoryteller,
        nbAsVoter                   = entity.nbAsVoter,
        nbFoundStoryteller          = entity.nbFoundStoryteller,
        nbStorytellerOptimalTurns   = entity.nbStorytellerOptimalTurns,
        totalPoints                 = entity.totalPoints,
        persisted                   = true,
        scoresheet                  = emptyList(),
        colorHex                    = entity.colorHex,
        emoji                       = entity.emoji
    )

    fun toEndGameEntities(players: List<PlayerBean>, winnerName: String?): List<PlayerEntity> =
        players.map { player ->
            PlayerEntity(
                name                        = player.name,
                nbGames                     = player.nbGames + 1,
                nbWins                      = player.nbWins + (if (player.name == winnerName) 1 else 0),
                nbAsStoryteller             = player.nbAsStoryteller,
                nbAsVoter                   = player.nbAsVoter,
                nbFoundStoryteller          = player.nbFoundStoryteller,
                nbStorytellerOptimalTurns   = player.nbStorytellerOptimalTurns,
                // totalPoints accumulates the score of this game on top of the historical total
                totalPoints                 = player.totalPoints + player.currentScore,
                colorHex                    = player.colorHex,
                emoji                       = player.emoji
            )
        }
}

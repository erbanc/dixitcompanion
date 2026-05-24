package fr.erban.dxitcompanion.db.player

import fr.erban.dxitcompanion.game.player.PlayerBean

object PlayerConverter {

    fun toEntity(playerBean: PlayerBean): PlayerEntity = PlayerEntity(
        name = playerBean.name,
        nbGames = playerBean.nbGames,
        nbWins = playerBean.nbWins,
        colorHex = playerBean.colorHex,
        emoji = playerBean.emoji
    )

    fun toBean(entity: PlayerEntity): PlayerBean = PlayerBean(
        name = entity.name,
        currentScore = 0,
        nbGames = entity.nbGames,
        nbWins = entity.nbWins,
        persisted = true,
        scoresheet = emptyList(),
        colorHex = entity.colorHex,
        emoji = entity.emoji
    )

    fun toEntities(playerBeans: List<PlayerBean>): List<PlayerEntity> =
        playerBeans.map { toEntity(it) }
}

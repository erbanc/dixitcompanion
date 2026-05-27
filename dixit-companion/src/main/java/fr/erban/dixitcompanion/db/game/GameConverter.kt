package fr.erban.dixitcompanion.db.game

import com.google.gson.Gson
import fr.erban.dixitcompanion.game.GameBean

object GameConverter {

    fun toEntity(gameBean: GameBean): GameEntity = GameEntity(
        finished = gameBean.finished,
        nbTurns = gameBean.currentTurn,
        nameWinner = gameBean.nameWinner,
        pointsToWin = gameBean.pointsToWin,
        scoreSheet = Gson().toJson(gameBean.getScoresheet()),
        startedAt = gameBean.startedAt,
        endedAt = gameBean.endedAt
    )
}

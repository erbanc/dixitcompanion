package fr.erban.dxitcompanion.db.game

import com.google.gson.Gson
import fr.erban.dxitcompanion.game.GameBean

object GameConverter {

    fun toEntity(gameBean: GameBean): GameEntity = GameEntity(
        finished = gameBean.finished,
        nbTurns = gameBean.currentTurn,
        nameWinner = gameBean.nameWinner,
        pointsToWin = gameBean.pointsToWin,
        scoreSheet = Gson().toJson(gameBean.getScoresheet())
    )
}

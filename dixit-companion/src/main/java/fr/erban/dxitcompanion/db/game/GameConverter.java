package fr.erban.dxitcompanion.db.game;

import com.google.gson.Gson;

import fr.erban.dxitcompanion.game.GameBean;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameConverter {

    public static GameEntity toEntity(final GameBean gameBean) {

        return GameEntity.builder()
                .finished(gameBean.isFinished())
                .nbTurns(gameBean.getCurrentTurn())
                .nameWinner(gameBean.getNameWinner())
                .pointsToWin(gameBean.getPointsToWin())
                .scoreSheet(gameBean.getScoresheet() != null ? new Gson().toJson(gameBean.getScoresheet()) : null)
                .build();
    }
}

package fr.erban.dxitcompanion.db.player;

import java.util.ArrayList;
import java.util.List;

import fr.erban.dxitcompanion.game.player.PlayerBean;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerConverter {

    public static PlayerEntity toEntity(final PlayerBean playerBean) {

        return PlayerEntity.builder()
                .name(playerBean.getName())
                .nbGames(playerBean.getNbGames())
                .nbWins(playerBean.getNbWins())
                .build();
    }

    public static PlayerBean toBean(final PlayerEntity playerEntity) {

        return PlayerBean.builder()
                .currentScore(0)
                .name(playerEntity.getName())
                .nbGames(playerEntity.getNbGames())
                .nbWins(playerEntity.getNbWins())
                .persisted(true)
                .scoresheet(new ArrayList<>())
                .build();
    }

    public static List<PlayerEntity> toEntities(final List<PlayerBean> playerBeans) {

        final List<PlayerEntity> entities = new ArrayList<>();

        for (PlayerBean playerBean : playerBeans) {
            entities.add(toEntity(playerBean));
        }

        return entities;
    }
}

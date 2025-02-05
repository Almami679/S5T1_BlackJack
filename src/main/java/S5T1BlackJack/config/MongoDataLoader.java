package S5T1BlackJack.config;

import S5T1BlackJack.entities.enumsEntities.statusGame;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class MongoDataLoader implements CommandLineRunner {

    private final GameRepository gameRepository;

    @Autowired
    public MongoDataLoader(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void run(String... args) {
        if (gameRepository.count() == 0) {
            List<Game> games = Arrays.asList(
                    new Game(1, new Date(System.currentTimeMillis()), statusGame.HOUSE_WINS),
                    new Game(2, new Date(System.currentTimeMillis()), statusGame.THE_GAME_WAS_DRAWN),
                    new Game(3, new Date(System.currentTimeMillis()), statusGame.HOUSE_WINS),
                    new Game(4, new Date(System.currentTimeMillis()), statusGame.PLAYER_WINS),
                    new Game(5, new Date(System.currentTimeMillis()), statusGame.PLAYER_WINS)
            );
            gameRepository.saveAll(games);
        }
    }
}

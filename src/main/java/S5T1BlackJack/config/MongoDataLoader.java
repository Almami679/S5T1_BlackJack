package S5T1BlackJack.config;

import S5T1BlackJack.entities.enumsEntities.statusGame;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Configuration
public class MongoDataLoader  {

    @Bean
    CommandLineRunner initDatabase(GameRepository gameRepository) {
        return args -> {
            gameRepository.deleteAll()
                    .thenMany(Flux.just(
                            new Game(1, new Date(System.currentTimeMillis()), statusGame.HOUSE_WINS),
                            new Game(2, new Date(System.currentTimeMillis()), statusGame.THE_GAME_WAS_DRAWN),
                            new Game(3, new Date(System.currentTimeMillis()), statusGame.HOUSE_WINS),
                            new Game(4, new Date(System.currentTimeMillis()), statusGame.PLAYER_WINS),
                            new Game(5, new Date(System.currentTimeMillis()), statusGame.PLAYER_WINS)
                    ))
                    .flatMap(gameRepository::save);
        };
    }
}


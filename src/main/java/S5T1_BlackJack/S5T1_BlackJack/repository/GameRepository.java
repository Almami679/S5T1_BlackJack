package S5T1_BlackJack.S5T1_BlackJack.repository;

import S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, Integer> {
}

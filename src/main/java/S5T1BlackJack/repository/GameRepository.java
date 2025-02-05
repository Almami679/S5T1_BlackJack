package S5T1BlackJack.repository;

import S5T1BlackJack.entities.mongoDb.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends MongoRepository<Game, Integer> {
    Optional<Game> findById(int id);
}

package S5T1BlackJack.repository;

import S5T1BlackJack.entities.mongoDb.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface GameRepository extends ReactiveMongoRepository<Game, Integer> {
    Mono<Game> findById(String id);
}

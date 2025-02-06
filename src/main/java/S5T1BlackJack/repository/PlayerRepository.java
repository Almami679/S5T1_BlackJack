package S5T1BlackJack.repository;

import S5T1BlackJack.entities.sql.Player;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface PlayerRepository extends R2dbcRepository<Player, Integer> {
    Mono<Player> findPlayerByName(String name);
}

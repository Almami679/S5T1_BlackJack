package S5T1_BlackJack.S5T1_BlackJack.repository;

import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findPlayerByName(String name);
}

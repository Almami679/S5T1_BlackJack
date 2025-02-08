package S5T1BlackJack.service.playerService;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.entities.sql.Player;
import S5T1BlackJack.exceptions.DuplicatedPlayerException;
import S5T1BlackJack.exceptions.PlayerNotFoundException;
import S5T1BlackJack.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class PlayerService implements PlayerServiceInteface {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Mono<Player> addPlayer(Player player) {
        return playerRepository.findPlayerByName(player.getName())
                .flatMap(existingPlayer -> Mono.error(new DuplicatedPlayerException(
                        "Entity with the name '" + player.getName() + "' already exists.")))
                .switchIfEmpty(playerRepository.save(player))
                .cast(Player.class);
    }

    public Mono<Player> createNewPlayer(PlayerDTO playerDTO) {
        Player newPlayer = new Player();
        newPlayer.setName(playerDTO.getName());
        newPlayer.setTotalBalance(10000);
        return addPlayer(newPlayer);
    }


    @Override
    public Mono<Player> updatePlayer(Player updatedPlayer) {
        return playerRepository.findById(updatedPlayer.getId())
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found with id " + updatedPlayer.getId())))
                .flatMap(dbPlayer -> {
                    dbPlayer.setName(updatedPlayer.getName());
                    return playerRepository.save(dbPlayer);
                });
    }


    @Override
    public Mono<Player> getPlayer(int id) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player not found with id " + id)));
    }

    @Override
    public Mono<Player> getPlayerByName(PlayerDTO playerName) {
        return playerRepository.findPlayerByName(playerName.getName());
    }

    public Mono<Boolean> checkPlayer(PlayerDTO playerName) {
        return getAllPlayers()
                .filter(player -> player.getName().equalsIgnoreCase(playerName.getName()))
                .hasElements();
    }


    @Override
    public Flux<Player> getAllPlayers() {
        return playerRepository.findAll();
    }
}

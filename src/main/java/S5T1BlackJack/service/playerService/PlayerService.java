package S5T1BlackJack.service.playerService;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.entities.sql.Player;
import S5T1BlackJack.exceptions.DuplicatedPlayerException;
import S5T1BlackJack.exceptions.PlayerNotFoundException;
import S5T1BlackJack.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return playerRepository.save(updatedPlayer);
    }


    @Override
    public Mono<Player> getPlayerByName(PlayerDTO playerName) {
        return playerRepository.findPlayerByName(playerName.getName());
    }


    @Override
    public Flux<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Mono<Player> changePlayerName(int playerId, String newName) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new PlayerNotFoundException("Player with ID " + playerId + " not found.")))
                .flatMap(existingPlayer ->
                        playerRepository.findPlayerByName(newName)
                                .flatMap(duplicatePlayer -> Mono.error(new DuplicatedPlayerException("The name '" + newName + "is not available")))
                                .switchIfEmpty(Mono.defer(() -> {
                                    existingPlayer.setName(newName);
                                    return playerRepository.save(existingPlayer);
                                }))
                                .thenReturn(existingPlayer)
                );
    }
}

package S5T1BlackJack.service.playerService;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.entities.sql.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerServiceInteface {

    Mono<Player> addPlayer(Player player);
    Mono<Player> updatePlayer(Player updatedPlayer);
    Mono<Player> getPlayerByName(PlayerDTO playerName);
    Flux<Player> getAllPlayers();
    Mono<Player> createNewPlayer(PlayerDTO playerDTO);
    Mono<Player> changePlayerName(int playerId, String newName);
    Mono<Player> addAmount(int playerId, int amount);
}

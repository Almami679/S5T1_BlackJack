package S5T1BlackJack.service.gameService;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.DTO.PlayerRankDTO;
import S5T1BlackJack.entities.enumsEntities.ActionType;
import S5T1BlackJack.entities.enumsEntities.statusGame;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.entities.mongoDb.Hand;
import S5T1BlackJack.entities.sql.Player;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameServiceInterface {

    Mono<Game> addGame(PlayerDTO playerDTO);
    Mono<Game> getGame(int id);
    Mono<Game> verifyBetAmount(Game game,int betAmount);
    Mono<Game> makeBet(int id, int amount);
    Mono<Player> changePlayerName(int playerId, String newName);
    Flux<PlayerRankDTO> getRanking();
    Mono<Void> deleteGame(int gameId);
    Mono<Player> addAmountInPlayer(int playerId, int amount);
}

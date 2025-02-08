package S5T1BlackJack.service.gameService;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.entities.enumsEntities.ActionType;
import S5T1BlackJack.entities.enumsEntities.statusGame;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.entities.mongoDb.Hand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameServiceInterface {

    Mono<Game> addGame(PlayerDTO playerDTO);
    Mono<Game> getGame(int id);
    Mono<Game> verifyBetAmount(Game game,int betAmount);
    Mono<Game> playTurn(Game game, ActionType action);
    Mono<Game> checkGameStatus(Game game);
    Mono<Game> dealerTurn(Game game);
    Mono<Game> makeBet(int id, int amount);
    Mono<statusGame> getGameStatus(Game game);
    Mono<Hand> getPlayerHand(Game game);
    Mono<Hand> getDealerHand(Game game);
    Flux<Game> getAllGames();

}

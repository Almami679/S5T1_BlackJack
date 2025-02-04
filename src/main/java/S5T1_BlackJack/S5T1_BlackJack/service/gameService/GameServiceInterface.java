package S5T1_BlackJack.S5T1_BlackJack.service.gameService;

import S5T1_BlackJack.S5T1_BlackJack.DTO.PlayerDTO;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.ActionType;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.statusGame;
import S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb.Game;
import S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb.Hand;
import reactor.core.publisher.Mono;

public interface GameServiceInterface {

    Mono<Game> addGame(PlayerDTO playerDTO);
    Mono<Game> getGame(int id);
    Mono<Void> newBet(double bet);

    Mono<Void> placeBet(double betAmount);

    Mono<Game> playTurn(Game game, ActionType action);
    Mono<statusGame> getGameStatus();
    Mono<Hand> getPlayerHand();
    Mono<Hand> getDealerHand();
    Mono<Double> getBalance();

}

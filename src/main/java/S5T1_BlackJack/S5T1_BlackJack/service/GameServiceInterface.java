package S5T1_BlackJack.S5T1_BlackJack.service;

import S5T1_BlackJack.S5T1_BlackJack.DTO.PlayerDTO;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.ActionType;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.statusGame;
import S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb.Game;
import S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb.Hand;
import reactor.core.publisher.Mono;

public interface GameServiceInterface {

    Mono<Game> addGame(PlayerDTO playerDTO);
    Mono<Game> getGame();
    Mono<Void> newBet(double bet);

    Mono<Void> placeBet(double betAmount);

    Mono<statusGame> playTurn(ActionType action);
    Mono<statusGame> getGameStatus();
    Mono<Hand> getPlayerHand();
    Mono<Hand> getDealerHand();
    Mono<Double> getBalance();

}

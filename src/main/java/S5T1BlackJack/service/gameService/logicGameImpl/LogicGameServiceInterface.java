package S5T1BlackJack.service.gameService.logicGameImpl;

import S5T1BlackJack.entities.enumsEntities.ActionType;
import S5T1BlackJack.entities.mongoDb.Game;
import reactor.core.publisher.Mono;

public interface LogicGameServiceInterface {

    Mono<Game> playTurn(Game game, ActionType action);
    Mono<Game> checkGameStatus(Game game);
    Mono<Game> dealerTurn(Game game);
}

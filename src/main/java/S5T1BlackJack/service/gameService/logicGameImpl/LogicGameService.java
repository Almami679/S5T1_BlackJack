package S5T1BlackJack.service.gameService.logicGameImpl;

import S5T1BlackJack.entities.enumsEntities.ActionType;
import S5T1BlackJack.entities.enumsEntities.statusGame;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.exceptions.GameHasFInishException;
import S5T1BlackJack.exceptions.GameHasNotBetException;
import S5T1BlackJack.repository.GameRepository;
import S5T1BlackJack.service.playerService.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LogicGameService implements LogicGameServiceInterface{

    @Autowired
    private PlayerService playerService;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameStatusEvaluatorService statusEvaluator;

    @Override
    public Mono<Game> playTurn(Game game, ActionType actionType) {
        if (game.getBet() <= 0) {
            return Mono.error(new GameHasNotBetException("Game has no Bet"));
        }

        if (game.getStatus() != statusGame.IN_GAME) {
            return Mono.error(new GameHasFInishException("This game status is: " + game.getStatus()));
        }

        Mono<Game> updatedGameMono;

        if (actionType == ActionType.STAND) {
            updatedGameMono = dealerTurn(game);
        } else {
            updatedGameMono = game.nextCard()
                    .flatMap(card -> game.getPlayerHand().addCard(card))
                    .flatMap(updatedHand -> {
                        game.setPlayerHand(updatedHand);
                        return checkGameStatus(game);
                    })
                    .thenReturn(game);
        }

        return updatedGameMono.flatMap(gameRepository::save);
    }

    @Override
    public Mono<Game> dealerTurn(Game game) {
        if (game.getStatus() != statusGame.IN_GAME) {
            return Mono.just(game);
        }
        return game.getDealerScore()
                .flatMap(score -> {
                    if (score < 17) {
                        return game.nextCard()
                                .flatMap(card -> game.getDealerHand().addCard(card))
                                .flatMap(updatedHand -> {
                                    game.setDealerHand(updatedHand);
                                    return dealerTurn(game);
                                });
                    }
                    return checkGameStatus(game);
                })
                .flatMap(gameRepository::save);
    }

    @Override
    public Mono<Game> checkGameStatus(Game game) {
        return game.getPlayerScore()
                .flatMap(playerScore -> game.getDealerScore()
                        .flatMap(dealerScore -> game.getPlayerHand().checkBlackjack()
                                .flatMap(playerHasBlackjack -> {

                                    game.setStatus(statusEvaluator.getFinalStatusGame(playerScore, dealerScore, playerHasBlackjack));

                                    return statusEvaluator.getFinalGameForStatus(game, game.getStatus());
                                })
                        )
                );
    }


}

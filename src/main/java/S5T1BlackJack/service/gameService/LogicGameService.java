package S5T1BlackJack.service.gameService;

import S5T1BlackJack.entities.enumsEntities.ActionType;
import S5T1BlackJack.entities.enumsEntities.statusGame;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.entities.sql.Player;
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
                                    statusGame output;

                                    // 1. Si el jugador se pasa de 21, pierde inmediatamente
                                    if (playerScore > 21) {
                                        output = statusGame.HOUSE_WINS;
                                    }
                                    // 2. Si el dealer aún no ha jugado, el juego sigue
                                    else if (game.getDealerHand().getCards().isEmpty()) {
                                        output = statusGame.IN_GAME;
                                    }
                                    // 3. Si el jugador tiene blackjack pero el dealer no, gana
                                    else if (playerHasBlackjack && dealerScore != 21) {
                                        output = statusGame.PLAYER_WINS;
                                    }
                                    // 4. Si el dealer se pasa de 21, el jugador gana
                                    else if (dealerScore > 21) {
                                        output = statusGame.PLAYER_WINS;
                                    }
                                    // 5. Si el jugador y el dealer tienen el mismo puntaje, empate
                                    else if (playerScore.equals(dealerScore)) {
                                        output = statusGame.THE_GAME_WAS_DRAWN;
                                    }
                                    // 6. Si el jugador tiene más puntos que el dealer, gana
                                    else if (playerScore > dealerScore) {
                                        output = statusGame.PLAYER_WINS;
                                    }
                                    // 7. Si el dealer tiene más puntos que el jugador, gana el dealer
                                    else {
                                        output = statusGame.HOUSE_WINS;
                                    }

                                    game.setStatus(output);


                                    if (output != statusGame.IN_GAME) {
                                        Player updatedPlayer = game.updateBalance(output);
                                        return playerService.updatePlayer(updatedPlayer)
                                                .then(gameRepository.save(game));
                                    } else {
                                        return Mono.just(game);
                                    }
                                })
                        )
                );
    }
}

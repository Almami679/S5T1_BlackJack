package S5T1_BlackJack.S5T1_BlackJack.service;

import S5T1_BlackJack.S5T1_BlackJack.DTO.PlayerDTO;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.ActionType;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.statusGame;
import S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb.Game;
import S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb.Hand;
import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Player;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GameService implements GameServiceInterface{

    private final Game game;

    public Mono<Void> addGame(PlayerDTO pLayerDTO) {

        Game game = new Game();
        game.
    }
    public GameService(Player player) {
        this.game = new Game(player);
    }

    public Mono<Game> getGame(){
        return Mono.just(game);
    }

    @Override
    public Mono<Void> newBet(double betAmount) {
        if (betAmount > game.getTotalBalance()) {
            return Mono.error(new IllegalArgumentException("Saldo insuficiente para apostar"));
        }
        return game.placeBet(betAmount);
    }

    @Override
    public Mono<Void> placeBet(double betAmount) {
        return game.placeBet(betAmount);
    }

    @Override
    public Mono<statusGame> playTurn(ActionType actionType) {
        if (game.getStatus() != statusGame.IN_GAME) {
            return Mono.just(game.getStatus());
        }

        if (actionType == ActionType.STAND) {
            return dealerTurn();
        }

        return game.nextCard()
                .flatMap(game.getPlayerHand()::addCard)
                .then(game.getPlayerScore())
                .flatMap(score -> {
                    if (score > 21) {
                        game.updateBalance(statusGame.HOUSE_WINS);
                        return Mono.just(statusGame.HOUSE_WINS);
                    }
                    return Mono.just(statusGame.IN_GAME);
                });
    }

    @Override
    public Mono<statusGame> getGameStatus() {
        return Mono.just(game.getStatus());
    }

    @Override
    public Mono<Hand> getPlayerHand() {
        return Mono.just(game.getPlayerHand());
    }

    @Override
    public Mono<Hand> getDealerHand() {
        return Mono.just(game.getDealerHand());
    }

    @Override
    public Mono<Double> getBalance() {
        return Mono.just(game.getTotalBalance());
    }

    private Mono<statusGame> dealerTurn() {
        return game.getDealerScore()
                .flatMap(score -> {
                    if (score < 17) {
                        return game.nextCard()
                                .flatMap(game.getDealerHand()::addCard)
                                .then(dealerTurn());
                    }
                    return determineWinner();
                });
    }

    private Mono<statusGame> determineWinner() {
        return game.getPlayerScore()
                .zipWith(game.getDealerScore(), (playerScore, dealerScore) -> {
                    statusGame result = playerScore > 21 ? statusGame.HOUSE_WINS :
                            dealerScore > 21 ? statusGame.PLAYER_WINS :
                                    playerScore > dealerScore ? statusGame.PLAYER_WINS :
                                            dealerScore > playerScore ? statusGame.HOUSE_WINS :
                                                    statusGame.THE_GAME_WAS_DRAWN;

                    game.updateBalance(result);
                    return result;
                });
    }
}



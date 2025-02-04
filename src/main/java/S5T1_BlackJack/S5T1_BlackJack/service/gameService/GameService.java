package S5T1_BlackJack.S5T1_BlackJack.service.gameService;

import S5T1_BlackJack.S5T1_BlackJack.DTO.PlayerDTO;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.ActionType;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.statusGame;
import S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb.Game;
import S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb.Hand;
import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Player;
import S5T1_BlackJack.S5T1_BlackJack.exceptions.DuplicatedPlayerException;
import S5T1_BlackJack.S5T1_BlackJack.exceptions.GameHasNotBetException;
import S5T1_BlackJack.S5T1_BlackJack.exceptions.GameNotFoundException;
import S5T1_BlackJack.S5T1_BlackJack.repository.GameRepository;
import S5T1_BlackJack.S5T1_BlackJack.service.playerService.PlayerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GameService implements GameServiceInterface {

    private final Game game;
    private PlayerService playerService;
    private GameRepository gameRepository;

    public Mono<Game> addGame(PlayerDTO playerDTO) {
        if(playerService.getPlayerByName(playerDTO) == null) {
            playerService.addPlayer(playerDTO);
            Player playerGame = new Player();
            playerGame.setName(playerDTO.name());
            Game game = new Game(playerGame);
            gameRepository.save(game);
            return Mono.just(game);

        } else {
            Game game = new Game(playerService.getPlayerByName(playerDTO));
            gameRepository.save(game);
            return Mono.just(game);
        }
    }
    public GameService(Player player) {
        this.game = new Game(player);
    }

    public Mono<Game> getGame(int id){
        return Mono.just(gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException("Fruit not found with id " + id)));

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
    public Mono<Game> playTurn(Game game, ActionType actionType) {
            if (game.getBet() > 0) {
                if (game.getStatus() != statusGame.IN_GAME) {
                    return Mono.just(game);
                }

                if (actionType == ActionType.STAND) {
                    return dealerTurn();
                }

                return game.nextCard()
                        .flatMap(game.getPlayerHand()::addCard)
                        .then(game.getPlayerScore())
                        .flatMap(score -> {
                            if (score > 21) {
                                game.setStatus(statusGame.HOUSE_WINS);
                                return Mono.just(game);
                            }
                            return Mono.just(game);
                        });
            } else  { throw new GameHasNotBetException("Game not has Bet");}
    }

    private Mono<Game> dealerTurn() {
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

    private Mono<Game> determineWinner() {
        return game.getPlayerScore()
                .flatMap(playerScore -> game.getDealerScore()
                        .map(dealerScore -> {
                            statusGame result;
                            if (playerScore > 21) {
                                result = statusGame.HOUSE_WINS;
                            } else if (dealerScore > 21) {
                                result = statusGame.PLAYER_WINS;
                            } else if (playerScore > dealerScore) {
                                result = statusGame.PLAYER_WINS;
                            } else if (dealerScore > playerScore) {
                                result = statusGame.HOUSE_WINS;
                            } else {
                                result = statusGame.THE_GAME_WAS_DRAWN;
                            }
                            game.updateBalance(result);
                            return game;
                        }));
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



}



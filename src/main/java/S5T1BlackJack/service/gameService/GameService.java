package S5T1BlackJack.service.gameService;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.entities.enumsEntities.ActionType;
import S5T1BlackJack.entities.enumsEntities.statusGame;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.entities.mongoDb.Hand;
import S5T1BlackJack.entities.sql.Player;
import S5T1BlackJack.exceptions.GameHasNotBetException;
import S5T1BlackJack.exceptions.GameNotFoundException;
import S5T1BlackJack.repository.GameRepository;
import S5T1BlackJack.service.playerService.PlayerService;
import S5T1BlackJack.service.playerService.PlayerServiceInteface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GameService implements GameServiceInterface {


    @Autowired
    private PlayerService playerService;
    @Autowired
    private GameRepository gameRepository;

    public GameService(PlayerService playerService, GameRepository gameRepository) {
        this.playerService = playerService;
        this.gameRepository = gameRepository;
    }

    public Mono<Game> addGame(PlayerDTO playerDTO) {
        return playerService.checkPlayer(playerDTO)
                .flatMap(playerExists -> {
                    if (!playerExists) {
                        Player playerGame = new Player();
                        playerGame.setName(playerDTO.getName());
                        playerGame.setTotalBalance(10000);

                        return playerService.addPlayer(playerGame)
                                .flatMap(savedPlayer -> getNextId()
                                        .flatMap(id -> {
                                            Game game = new Game((Player) savedPlayer);
                                            game.setId(id);
                                            return gameRepository.save(game);
                                        }));
                    } else {
                        return playerService.getPlayerByName(playerDTO)
                                .flatMap(existingPlayer -> getNextId()
                                        .flatMap(id -> {
                                            Game game = new Game(existingPlayer);
                                            game.setId(id);
                                            return gameRepository.save(game);
                                        }));
                    }
                });
    }


    public Mono<Game> getGame(int id){
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with id: " + id)));

    }

    public Mono<Game> makeBet(int id, int amount) {
        return getGame(id)
                .flatMap(game -> verifyBetAmount(game, amount))
                .flatMap(updatedGame -> gameRepository.save(updatedGame));
    }

    public Mono<Game> verifyBetAmount(Game game, int betAmount) {
        return game.getPlayer()
                .map(Player::getTotalBalance)
                .flatMap(balance -> {
                    if (balance == null || betAmount > balance) {
                        return Mono.error(new IllegalArgumentException("You don't have enough credits"));
                    }
                    game.setBet(betAmount);
                    return Mono.just(game);
                });
    }

    @Override
    public Mono<Game> playTurn(Game game, ActionType actionType) {
        if (game.getBet() <= 0) {
            return Mono.error(new GameHasNotBetException("Game has no Bet"));
        }

        if (game.getStatus() != statusGame.IN_GAME) {
            return Mono.just(game);
        }

        if (actionType == ActionType.STAND) {
            return dealerTurn(game);
        }

        return game.nextCard()
                .flatMap(game.getPlayerHand()::addCard)
                .then(checkGameStatus(game));
    }

    @Override
    public Mono<Game> dealerTurn(Game game) {
        return game.getDealerScore()
                .flatMap(score -> {
                    if (score < 17) {
                        return game.nextCard()
                                .flatMap(game.getDealerHand()::addCard)
                                .then(dealerTurn(game));
                    }
                    return checkGameStatus(game);
                });
    }

    @Override
    public Mono<Game> checkGameStatus(Game game) {
        return game.getPlayerScore()
                .flatMap(playerScore -> game.getDealerScore()
                        .flatMap(dealerScore -> game.getPlayerHand().checkBlackjack()
                                .flatMap(playerHasBlackjack -> {
                                    statusGame output;

                                    if (playerHasBlackjack) {
                                        output = statusGame.PLAYER_WINS;
                                    } else if (playerScore > 21) {
                                        output = statusGame.HOUSE_WINS;
                                    } else if (dealerScore > 21) {
                                        output = statusGame.PLAYER_WINS;
                                    } else if (playerScore > dealerScore) {
                                        output = statusGame.PLAYER_WINS;
                                    } else if (dealerScore > playerScore) {
                                        output = statusGame.HOUSE_WINS;
                                    } else {
                                        output = statusGame.THE_GAME_WAS_DRAWN;
                                    }

                                    game.setStatus(output);
                                    game.updateBalance(output);

                                    return Mono.just(game);
                                })
                        )
                );
    }

    @Override
    public Mono<statusGame> getGameStatus(Game game) {
        return Mono.just(game.getStatus());
    }

    @Override
    public Mono<Hand> getPlayerHand(Game game) {
        return Mono.just(game.getPlayerHand());
    }

    @Override
    public Mono<Hand> getDealerHand(Game game) {
        return Mono.just(game.getDealerHand());
    }

    public Flux<Game> getAllGames(){
       return (Flux<Game>) gameRepository.findAll();
    }

    public Mono<Integer> getNextId() {
        return gameRepository.findAll()
                .map(Game::getId)
                .collectList()
                .map(ids -> ids.stream().mapToInt(i -> i).max().orElse(0) + 1);
    }


}



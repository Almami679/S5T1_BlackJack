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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GameService implements GameServiceInterface {

    private final Game game;
    private PlayerService playerService;
    private GameRepository gameRepository;

    public Mono<Game> addGame(PlayerDTO playerDTO) {
        if(!playerService.checkPlayer(playerDTO)) {
            playerService.addPlayer(playerDTO);
            Player playerGame = new Player();
            playerGame.setName(playerDTO.getName());
            Game game = new Game(playerGame);
            game.setId(getNextId().block());
            gameRepository.save(game);
            return Mono.just(game);

        } else {
            Game game = new Game(playerService.getPlayerByName(playerDTO));
            game.setId(getNextId().block());
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
            return dealerTurn();
        }

        return game.nextCard()
                .flatMap(game.getPlayerHand()::addCard)
                .then(checkGameStatus());
    }

    @Override
    public Mono<Game> dealerTurn() {
        return game.getDealerScore()
                .flatMap(score -> {
                    if (score < 17) {
                        return game.nextCard()
                                .flatMap(game.getDealerHand()::addCard)
                                .then(dealerTurn());
                    }
                    return checkGameStatus();
                });
    }

    @Override
    public Mono<Game> checkGameStatus() {
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

    public Flux<Game> getAllGames(){
       return (Flux<Game>) gameRepository.findAll();
    }

    public Mono<Integer> getNextId(){
        return getAllGames().collectList()
                .map(game -> game.stream()
                        .mapToInt(Game::getId)
                .max()
                .orElse(0) + 1);
    }


}



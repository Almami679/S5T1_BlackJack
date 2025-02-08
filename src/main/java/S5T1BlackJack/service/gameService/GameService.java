package S5T1BlackJack.service.gameService;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.DTO.PlayerRankDTO;
import S5T1BlackJack.entities.enumsEntities.ActionType;
import S5T1BlackJack.entities.enumsEntities.statusGame;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.entities.mongoDb.Hand;
import S5T1BlackJack.entities.sql.Player;
import S5T1BlackJack.exceptions.GameHasFInishException;
import S5T1BlackJack.exceptions.GameHasNotBetException;
import S5T1BlackJack.exceptions.GameNotFoundException;
import S5T1BlackJack.repository.GameRepository;
import S5T1BlackJack.service.playerService.PlayerService;
import S5T1BlackJack.service.playerService.PlayerServiceInteface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Objects;

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
        return playerService.getPlayerByName(playerDTO)
                .switchIfEmpty(playerService.createNewPlayer(playerDTO))
                .flatMap(player -> getNextId()
                        .flatMap(id -> {
                            Game game = new Game(player);
                            game.setId(id);
                            return gameRepository.save(game);
                        })
                );
    }


    public Mono<Game> getGame(int id){
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game not found with id: " + id)));

    }

    public Mono<Game> makeBet(int id, int amount) {
        return getGame(id)
                .flatMap(game -> verifyBetAmount(game, amount))
                .flatMap(game -> {
                    game.setBet(amount);
                    return gameRepository.save(game);
                });
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

    public Flux<Game> getAllGames(){
       return (Flux<Game>) gameRepository.findAll();
    }

    public Mono<Integer> getNextId() {
        return gameRepository.findAll()
                .map(Game::getId)
                .collectList()
                .map(ids -> ids.stream().mapToInt(i -> i).max().orElse(0) + 1);
    }

    public Flux<PlayerRankDTO> getRanking() {
        return playerService.getAllPlayers()
                .sort(Comparator.comparingInt(Player::getScore).reversed())
                .map(player -> new PlayerRankDTO(player.getName(), (int) player.getScore()));
    }

    public Mono<Void> deleteGame(int gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with ID " + gameId + " not found.")))
                .flatMap(existingGame -> gameRepository.delete(existingGame));
    }

    public Mono<Player> changePlayerName(int playerId, String newName) {
        return playerService.changePlayerName(playerId, newName);
    }


}



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

    public Mono<Player> addAmountInPlayer(int playerId, int amount) {
        return playerService.addAmount(playerId, amount);
    }


}



package S5T1_BlackJack.S5T1_BlackJack.controllers;

import S5T1_BlackJack.S5T1_BlackJack.DTO.PlayerDTO;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.ActionType;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.statusGame;
import S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb.Game;
import S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb.Hand;
import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Player;
import S5T1_BlackJack.S5T1_BlackJack.exceptions.DuplicatedPlayerException;
import S5T1_BlackJack.S5T1_BlackJack.service.gameService.GameService;
import S5T1_BlackJack.S5T1_BlackJack.service.gameService.GameServiceInterface;
import S5T1_BlackJack.S5T1_BlackJack.service.playerService.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/blackJack")
public class GameController {

    @Autowired
    private GameServiceInterface gameService;


    public GameController(GameServiceInterface gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game/new")
    public Mono<ResponseEntity<Game>> newGame(@RequestBody PlayerDTO playerDTO) {
        return gameService.addGame(playerDTO)
                .map(game -> ResponseEntity.status(HttpStatus.CREATED).body(game));
    }

    @GetMapping("/game/{id}")
    public Mono<ResponseEntity<Game>> getOneFruit(@PathVariable int id) {
       return gameService.getGame(id).map(ResponseEntity::ok);
    }



    @PostMapping("/game/{id}/play/{action}")
    public Mono<ResponseEntity<Game>> playTurn(@PathVariable int id, ActionType action) {
        return gameService.playTurn(gameService.getGame(id).block(),action).map(ResponseEntity::ok);
    }

    @GetMapping("/status")
    public Mono<statusGame> getGameStatus() {
        return gameService.getGameStatus();
    }

    @GetMapping("/player-hand")
    public Mono<Hand> getPlayerHand() {
        return gameService.getPlayerHand();
    }

    @GetMapping("/dealer-hand")
    public Mono<Hand> getDealerHand() {
        return gameService.getDealerHand();
    }

    @GetMapping("/balance")
    public Mono<Double> getBalance() {
        return gameService.getBalance();
    }
}


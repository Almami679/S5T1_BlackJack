package S5T1_BlackJack.S5T1_BlackJack.controllers;

import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.ActionType;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.statusGame;
import S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb.Hand;
import S5T1_BlackJack.S5T1_BlackJack.service.GameService;
import S5T1_BlackJack.S5T1_BlackJack.service.GameServiceInterface;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/blackJack")
public class GameController {

    private GameServiceInterface gameService;

    public GameController(GameServiceInterface gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game/new{player}")
    public Mono<Void> newBet(@PathVariable String player) {
        gameService = new GameService(player);
        return gameService.getGame();
    }

    @PostMapping("/play/{action}")
    public Mono<statusGame> playTurn(@PathVariable ActionType action) {
        return gameService.playTurn(action);
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


package S5T1BlackJack.controllers;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.entities.enumsEntities.ActionType;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.entities.sql.Player;
import S5T1BlackJack.service.gameService.GameServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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

    @Operation(summary = "Create a new Blackjack game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created Successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "400", description = "Error")
    })
    @GetMapping("/game/{id}")
    public Mono<ResponseEntity<Game>> getOneFruit(@PathVariable int id) {
       return gameService.getGame(id).map(ResponseEntity::ok);
    }



    @Operation(summary = "Play this game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Play success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "404", description = "Error")
    })
    @PostMapping("/game/{id}/play/{action}")
    public Mono<ResponseEntity<Game>> playTurn(@PathVariable int id, ActionType action) {
        return gameService.playTurn(gameService.getGame(id).block(),action).map(ResponseEntity::ok);
    }


    @Operation(summary = "Make a new bet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bet created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "404", description = "Error")
    })
    @PostMapping("/game/{id}/bet/{amount}")
    public Mono<ResponseEntity<Game>> makeBet(@PathVariable int id, int amount) {
        return gameService.verifyBetAmount(gameService.getGame(id).block(), amount)
                .map(game -> ResponseEntity.status(HttpStatus.CREATED).body(game));
    }

    @Operation(summary = "Get Ranking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "404", description = "Error")
    })
    @GetMapping("/ranking")
    public Flux<Player> getRankng() {
        return Flux.empty();
    }
}


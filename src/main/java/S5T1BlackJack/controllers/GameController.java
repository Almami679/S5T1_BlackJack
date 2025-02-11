package S5T1BlackJack.controllers;

import S5T1BlackJack.DTO.AmountDTO;
import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.DTO.PlayerRankDTO;
import S5T1BlackJack.entities.enumsEntities.ActionType;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.entities.sql.Player;
import S5T1BlackJack.exceptions.GameNotFoundException;
import S5T1BlackJack.service.gameService.GameServiceInterface;
import S5T1BlackJack.service.gameService.LogicGameServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/blackJack")
public class GameController {


    private final GameServiceInterface gameService;

    private final LogicGameServiceInterface logicGame;

    @Autowired
    public GameController(GameServiceInterface gameService, LogicGameServiceInterface logicGame) {
        this.gameService = gameService;
        this.logicGame = logicGame;
    }

    @Operation(summary = "Create a new Blackjack game (FUNCTIONALLY)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created Successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "400", description = "Error")
    })
    @PostMapping("/game/new")
    public Mono<ResponseEntity<Game>> newGame(@Valid @RequestBody PlayerDTO playerDTO) {
        return gameService.addGame(playerDTO)
                .map(game -> ResponseEntity.status(HttpStatus.CREATED).body(game));
    }

    @Operation(summary = "Find Blackjack game (FUNCTIONALLY)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created Successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "400", description = "Error")
    })
    @GetMapping("/game/{id}")
    public Mono<ResponseEntity<Game>> getGame(@PathVariable int id) {
       return gameService.getGame(id).map(ResponseEntity::ok);
    }



    @Operation(summary = "Play this game (FUNCTIONALLY)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Play success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "400", description = "Invalid action type"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @PostMapping("/game/{id}/play/{action}")
    public Mono<ResponseEntity<Game>> playTurn(@PathVariable int id,
                                               @Valid @PathVariable ActionType action) {
        return gameService.getGame(id)
                .flatMap(game -> logicGame.playTurn(game, action))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }



    @Operation(summary = "Make a new bet (FUNCTIONALLY)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bet created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Game.class))),
            @ApiResponse(responseCode = "404", description = "Error")
    })
    @PostMapping("/game/{id}/bet/{amount}")
    public Mono<ResponseEntity<Game>> makeBet(@PathVariable int id, @PathVariable int amount) {
        return gameService.makeBet(id, amount)
                .map(updatedGame -> ResponseEntity.status(HttpStatus.CREATED).body(updatedGame))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }


    @Operation(summary = "Get Ranking (FUNCTIONALLY)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "404", description = "Error")
    })
    @GetMapping("/ranking")
    public Mono<ResponseEntity<Flux<PlayerRankDTO>>> getRanking() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(gameService.getRanking())
        );
    }


    @Operation(summary = "Delete a game by ID (FUNCTIONALLY)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @DeleteMapping("/game/{id}")
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable int id) {
        return gameService.deleteGame(id)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(GameNotFoundException.class, e -> Mono.just(ResponseEntity.notFound().<Void>build()));
    }


    @Operation(summary = "Change player's name (FUNCTIONALLY)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player name updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "404", description = "Player not found"),
            @ApiResponse(responseCode = "409", description = "Duplicated player name")
    })
    @PutMapping("/player/{id}/name")
    public Mono<ResponseEntity<Player>> changePlayerName(@PathVariable int id, @RequestParam String newName) {
        return gameService.changePlayerName(id, newName)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Add amount in player Wallet (FUNCTIONALLY)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "amount updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Player.class))),
            @ApiResponse(responseCode = "404", description = "Player not found"),
    })
    @PutMapping("/{id}/amount")
    public Mono<ResponseEntity<Player>> addAmountToPlayer(
            @PathVariable int id,
            @RequestBody @Valid AmountDTO amountDTO) {
        return gameService.addAmountInPlayer(id, amountDTO.getAmount())
                .map(ResponseEntity::ok);
    }
}


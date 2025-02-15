package S5T1_BlackJack.S5T1_BlackJack.ControllerTests;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.DTO.PlayerRankDTO;
import S5T1BlackJack.S05T01N01Application;
import S5T1BlackJack.controllers.GameController;
import S5T1BlackJack.entities.enumsEntities.ActionType;
import S5T1BlackJack.entities.enumsEntities.statusGame;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.entities.localEntities.Hand;
import S5T1BlackJack.entities.sql.Player;
import S5T1BlackJack.repository.GameRepository;
import S5T1BlackJack.service.gameService.GameServiceInterface;
import S5T1BlackJack.service.gameService.logicGameImpl.LogicGameServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = S05T01N01Application.class)
@SpringBootTest
@ActiveProfiles("games-test")
@AutoConfigureWebTestClient

class PhpResponseTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Autowired
    private R2dbcEntityTemplate sqlTemplate;

    @Mock
    private GameServiceInterface gameService;

    @Mock
    private GameController gameController;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private LogicGameServiceInterface logicGame;

    private static Game mockGame = new Game(1,
            new Date(System.currentTimeMillis()),
            statusGame.IN_GAME,
            new Hand(), new Hand(),
            new Player("Carlitos"));


    @BeforeEach
    void setUp() {
        gameService = Mockito.mock(GameServiceInterface.class);
    }

    void resetDatabases() {

        mongoTemplate.getCollectionNames()
                .flatMap(mongoTemplate::dropCollection)
                .blockLast();

        sqlTemplate.getDatabaseClient().sql("DELETE FROM players").then().block();
    }

    @Test
    void createNewGame_shouldReturn201() {
        resetDatabases();
        PlayerDTO playerDTO = new PlayerDTO("Carlitos");

        when(gameService.addGame(any(PlayerDTO.class))).thenReturn(Mono.just(mockGame));

        webTestClient.post()
                .uri("/blackJack/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(playerDTO)
                .exchange()
                .expectStatus().isCreated();

    }

    @Test
    void findGameById_shouldReturn200() {
        int searchId = 1;

        when(gameService.getGame(searchId)).thenReturn(Mono.just(mockGame));

        webTestClient.get()
                .uri("/blackJack/game/" + searchId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    void makeBet_shouldReturn201() {
        int searchId = 1;
        int bet = 100;


        when(gameService.makeBet(1, 100)).thenReturn(Mono.just(mockGame));

        webTestClient.post()
                .uri("/blackJack/game/" + searchId + "/bet/" + bet)
                .exchange()
                .expectStatus().isCreated();

    }

    @Test
    void playTurn_shouldReturn200() {
        ActionType actionType = ActionType.HIT;
        int searchId = 1;

        when(gameService.getGame(searchId)).thenReturn(Mono.just(mockGame));


        webTestClient.post()
                .uri("/blackJack/game/" + searchId + "/play/" + actionType)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    void getRanking_PHPResponse() {
        when(gameService.getRanking()).thenReturn(Flux.just(new PlayerRankDTO("Irenita", 3000)));

        webTestClient.get()
                .uri("/blackJack/ranking")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void deleteGame_shouldReturn200() {
        when(gameService.deleteGame(1)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/blackJack/game/1")
                .exchange()
                .expectStatus().isOk();
    }


}

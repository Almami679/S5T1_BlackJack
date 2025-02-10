package S5T1_BlackJack.S5T1_BlackJack.ControllerTests;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.DTO.PlayerRankDTO;
import S5T1BlackJack.S05T01N01Application;
import S5T1BlackJack.controllers.GameController;
import S5T1BlackJack.entities.sql.Player;
import S5T1BlackJack.repository.PlayerRepository;
import S5T1BlackJack.service.gameService.GameService;
import S5T1BlackJack.service.gameService.GameServiceInterface;
import S5T1BlackJack.service.gameService.LogicGameServiceInterface;
import S5T1BlackJack.service.playerService.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
//import org.springframework.boot.test.mock.mockito.MockBean; DEPRECATED
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
@ContextConfiguration(classes = S05T01N01Application.class)
@ExtendWith(MockitoExtension.class)
class ExceptionHandlerTests {


    private WebTestClient webTestClient;

    @Mock
    private GameService gameService;
    @InjectMocks
    private GameController gameController;

    private ArrayList<PlayerRankDTO> playersRanks;
    private ArrayList<PlayerDTO> playersDTO;

    @BeforeEach
    void constructor(){
        playersRanks = new ArrayList<>(
                Arrays.asList(
                        new PlayerRankDTO("Irenita", 3000),
                        new PlayerRankDTO("Albert", 2000)));
        playersDTO = new ArrayList<>(
                Arrays.asList(
                        new PlayerDTO("Irenita"),
                        new PlayerDTO("Albert")));
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRanking_shouldReturnFluxOfPlayers() {

        when(gameService.getRanking()).thenReturn(playersRanks.forEach(player -> Flux.just(player)));

        webTestClient.get()
                .uri("/ranking")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBodyList(PlayerRankDTO.class)
                .hasSize(2)
                .contains(playersRanks.get(0), playersRanks.get(1));
    }
}


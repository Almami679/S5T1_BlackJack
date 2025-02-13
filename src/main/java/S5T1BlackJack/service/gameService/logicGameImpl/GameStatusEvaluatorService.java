package S5T1BlackJack.service.gameService.logicGameImpl;

import S5T1BlackJack.entities.enumsEntities.statusGame;
import S5T1BlackJack.entities.mongoDb.Game;
import S5T1BlackJack.entities.sql.Player;
import S5T1BlackJack.repository.GameRepository;
import S5T1BlackJack.service.playerService.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GameStatusEvaluatorService {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private GameRepository gameRepository;

    public Mono<Game> getFinalGameForStatus(Game game, statusGame output) {
        if (output != statusGame.IN_GAME) {
            Player updatedPlayer = game.updateBalance(output);
            return playerService.updatePlayer(updatedPlayer)
                    .then(gameRepository.save(game));
        } else {
            return Mono.just(game);
        }
    }

    public statusGame getFinalStatusGame(Integer playerScore, Integer dealerScore, Boolean playerHasBlackjack) {
        statusGame output;
        if(checkPlayerWin(playerHasBlackjack, dealerScore, playerScore)){
            output = statusGame.PLAYER_WINS;
        }
        else if (playerScore.equals(dealerScore)) {
            output = statusGame.THE_GAME_WAS_DRAWN;
        }
        else {
            if(dealerScore > 0) {
                output = statusGame.HOUSE_WINS;
            } else {
                output = statusGame.IN_GAME;
            }
        }
        return output;
    }

    private boolean checkPlayerWin(boolean blackJack, int dealerScore, int playerScore) {
        boolean output = false;
        if (blackJack && dealerScore != 21 && dealerScore != 0) {
            output = true;
        }
        else if (dealerScore > 21) {
            output = true;
        }
        else if (playerScore > dealerScore && playerScore <= 21 && dealerScore != 0) {
            output = true;
        }
        return output;
    }
}

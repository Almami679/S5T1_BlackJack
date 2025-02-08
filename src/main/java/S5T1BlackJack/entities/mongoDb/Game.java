package S5T1BlackJack.entities.mongoDb;

import S5T1BlackJack.entities.Deck;
import S5T1BlackJack.entities.enumsEntities.statusGame;
import S5T1BlackJack.entities.Card;
import S5T1BlackJack.entities.sql.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;

@Data
@Getter
@Setter
@Component
@Document(collection = "games")
@Schema(description = "Game entity representing a blackjack game session")
public class Game {

    @Id
    @Schema(description = "Game ID", example = "1")
    private int id;

    @Schema(description = "Player in the game")
    private Player player;

    @Schema(description = "Game creation date")
    private Date gameDate;

    @JsonIgnore
    @Schema(description = "Deck of cards used in the game", hidden = true)
    private final Deck deck = new Deck();

    @Schema(description = "Player's hand of cards")
    private final Hand playerHand = new Hand();

    @Schema(description = "Dealer's hand of cards")
    private final Hand dealerHand = new Hand();

    @Schema(description = "Current game status")
    private statusGame status;

    @Schema(description = "Bet amount for the game", example = "100")
    private int bet;

    public Game(){

        this.gameDate = new Date(System.currentTimeMillis());

        this.status = statusGame.IN_GAME;
        this.bet = 0;
    };

    public Game(Player player) {
        this.player = player;
        this.gameDate = new Date(System.currentTimeMillis());
        this.status = statusGame.IN_GAME;
        this.bet = 0;
    }

    public Game(int id, Date gameDate, statusGame status){
        this.id = id;
        this.gameDate = gameDate;
        this.status = status;
        this.player = null;

    }

    public void updateBalance(statusGame finalStatus) {
        int playerBalance = player.getTotalBalance();
        switch (finalStatus) {
            case PLAYER_WINS:
                player.setTotalBalance(playerBalance + (bet * 2));
                break;
            case HOUSE_WINS:
                player.setTotalBalance(playerBalance - bet);
                break;
            case THE_GAME_WAS_DRAWN:
                break;
        }
        this.status = finalStatus;
    }

    public Mono<Player> getPlayer() {
        return Mono.just(player);
    }


    public Mono<Card> nextCard() {
        return deck.nextCard();
    }


    public Mono<Integer> getPlayerScore() {
        return playerHand.calculateScore();
    }


    public Mono<Integer> getDealerScore() {
        return dealerHand.calculateScore();
    }


    public double getBet() {
        return bet;
    }

    public int getId() {
        return id;
    }

    public Game setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public Game setGameDate(Date gameDate) {
        this.gameDate = gameDate;
        return this;
    }

    public Deck getDeck() {
        return deck;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public statusGame getStatus() {
        return status;
    }

    public Game setStatus(statusGame status) {
        this.status = status;
        return this;
    }

    public Game setBet(int bet) {
        this.bet = bet;
        return this;
    }
}



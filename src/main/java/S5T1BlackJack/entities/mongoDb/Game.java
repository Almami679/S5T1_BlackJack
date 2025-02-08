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

    @Getter
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

    @Getter
    @Schema(description = "Player's hand of cards")
    private Hand playerHand = new Hand();

    @Getter
    @Schema(description = "Dealer's hand of cards")
    private Hand dealerHand = new Hand();

    @Getter
    @Schema(description = "Current game status")
    private statusGame status;

    @Schema(description = "Bet amount")
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

    public Game(int id, Date gameDate, statusGame status, Hand playerHand, Hand dealerHand, Player player){
        this.id = id;
        this.gameDate = gameDate;
        this.status = status;
        this.player = player;
        this.dealerHand = dealerHand;
        this.playerHand = playerHand;

    }

    public Player updateBalance(statusGame finalStatus) {
        int playerBalance = player.getTotalBalance();

        switch (finalStatus) {
            case PLAYER_WINS:
                player.setTotalBalance(playerBalance + bet);
                player.setWinGames(player.getWinGames() + 1);
                break;
            case HOUSE_WINS:
                player.setTotalBalance(playerBalance - bet);
                player.setLostGames(player.getLostGames() + 1);
                break;
            case THE_GAME_WAS_DRAWN:
                break;
        }

        player.setScore();

        return player;
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

    public void setPlayerHand(Hand updatedHand) {
        if (updatedHand != null) {
            this.playerHand = updatedHand;
        }
    }

    public void setDealerHand(Hand updatedHand) {
        if (updatedHand != null) {
            this.dealerHand = updatedHand;
        }
    }

    public double getBet() {
        return bet;
    }

    public Game setPlayer(Player player) {
        this.player = player;
        return this;
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



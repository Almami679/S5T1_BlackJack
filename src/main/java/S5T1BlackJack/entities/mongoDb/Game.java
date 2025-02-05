package S5T1BlackJack.entities.mongoDb;

import S5T1BlackJack.entities.Deck;
import S5T1BlackJack.entities.enumsEntities.statusGame;
import S5T1BlackJack.entities.Card;
import S5T1BlackJack.entities.sql.Player;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

import java.util.Date;

@Data
@Getter
@Setter
@Document(collection = "games") // Define la colección en MongoDB
public class Game {
    @Id
    private int id;
    private Player player;
    private Date gameDate;
    private final Deck deck;
    private final Hand playerHand;
    private final Hand dealerHand;
    private statusGame status;
    private int bet;

    public Game(){
        this.deck = new Deck();
        this.gameDate = new Date(System.currentTimeMillis());
        this.playerHand = new Hand();
        this.dealerHand = new Hand();
        this.status = statusGame.IN_GAME;
        this.bet = 0;
    };

    public Game(Player player) {
        this.player = player;
        this.gameDate = new Date(System.currentTimeMillis());
        this.deck = new Deck();
        this.playerHand = new Hand();
        this.dealerHand = new Hand();
        this.status = statusGame.IN_GAME;
        this.bet = 0;
    }

    public Game(int id, Date gameDate, statusGame status){
        this.id = id;
        this.gameDate = gameDate;
        this.status = status;
        this.player = null;
        this.playerHand = null;
        this.dealerHand = null;
        this.deck = null;
    }

    public void updateBalance(statusGame finalStatus) {
        int playerBalance = player.getTotalBalance();
        switch (finalStatus) {
            case PLAYER_WINS:
                player.setTotalBalance(playerBalance + bet);
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

    public Game setId(int id) {
        this.id = id;
        return this;
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



package S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb;

import S5T1_BlackJack.S5T1_BlackJack.entities.Deck;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.statusGame;
import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Card;
import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Player;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;


@Document(collection = "games") // Define la colección en MongoDB
public class Game {
    private Player player;
    private final Deck deck;
    private final Hand playerHand;
    private final Hand dealerHand;
    private statusGame status;
    private static double totalBalance = 1000;
    private double bet;

    public Game(){
        this.deck = new Deck();
        this.playerHand = new Hand();
        this.dealerHand = new Hand();
        this.status = statusGame.IN_GAME;
        this.bet = 0;
    };

    public Game(Player player) {
        this.player = player;
        this.deck = new Deck();
        this.playerHand = new Hand();
        this.dealerHand = new Hand();
        this.status = statusGame.IN_GAME;
        this.bet = 0;
    }


    public Mono<Void> placeBet(double betAmount) {
        if (betAmount > totalBalance) {
            return Mono.error(new IllegalArgumentException("you don't have enough credits"));
        }
        this.bet = betAmount;
        return Mono.empty();
    }


    public void updateBalance(statusGame finalStatus) {
        switch (finalStatus) {
            case PLAYER_WINS:
                totalBalance += bet;
                break;
            case HOUSE_WINS:
                totalBalance -= bet;
                break;
            case THE_GAME_WAS_DRAWN:
                // No se cambia el saldo en empate
                break;
        }
        this.status = finalStatus;
    }

    public Mono<Player> getPlayer() {
        return Mono.just(player);
    }

    public void setPlayer(Player player) {
        this.player = player;
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


    public Hand getPlayerHand() {
        return playerHand;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public statusGame getStatus() {
        return status;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public double getBet() {
        return bet;
    }
}



package S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb;

import S5T1_BlackJack.S5T1_BlackJack.entities.Deck;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.statusGame;
import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Card;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "games") // Define la colección en MongoDB
public class Game {
    @Id
    private int id;
    private int playerId;
    private Hand playerHand;
    private Hand dealerHand;
    private statusGame status;
    private Date createdAt;
    private Deck mainDeck;

    public Game() {}

    public Game(int id, int playerId, Hand playerHand, Hand dealerHand, statusGame status, Date createdAt) {
        this.id = id;
        this.playerId = playerId;
        this.playerHand = playerHand;
        this.dealerHand = dealerHand;
        this.status = status;
        this.createdAt = createdAt;
        this.mainDeck = new Deck();
    }

    public Mono<Void> playTurn(boolean isPlayerTurn) {
        return (isPlayerTurn ? playerHand.lostGame() : dealerHand.calculateScore())
                .flatMap(result -> {
                    if (isPlayerTurn) {
                        if (Boolean.TRUE.equals(result)) {
                            status = statusGame.HOUSE_WINS;
                            return Mono.empty();
                        }
                        return playTurn(false);
                    } else {
                        return dealerPlays().then(determineWinner().doOnNext(finalStatus -> this.status = finalStatus).then());
                    }
                });
    }


    private Mono<Void> dealerPlays() {
        return dealerHand.calculateScore()
                .flatMap(score -> {
                    if (score < 17) {
                        Card newCard = mainDeck.nextCard().block();
                        dealerHand.addCard(newCard);

                        return dealerPlays();
                    }
                    return Mono.empty();
                });
    }

    public Mono<statusGame> determineWinner() {
        return playerHand.calculateScore()
                .zipWith(dealerHand.calculateScore(), (playerScore, dealerScore) ->
                        playerScore > 21 ? statusGame.HOUSE_WINS :
                                dealerScore > 21 ? statusGame.PLAYER_WINS :
                                        playerScore > dealerScore ? statusGame.PLAYER_WINS :
                                                dealerScore > playerScore ? statusGame.HOUSE_WINS :
                                                        statusGame.THE_GAME_WAS_DRAWN
                );
    }

    public Mono<Boolean> isGameOver() {
        return Mono.just(status != statusGame.IN_GAME);
    }

    // Getters y Setters
    public int getId() { return id; }
    public int getPlayerId() { return playerId; }
    public Hand getPlayerHand() { return playerHand; }
    public Hand getDealerHand() { return dealerHand; }
    public statusGame getStatus() { return status; }
    public Date getCreatedAt() { return createdAt; }

}

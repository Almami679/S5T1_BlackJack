package S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb;

import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.CardValue;
import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Card;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

import java.util.List;

@Document(collection = "hands")
public class Hand {
    @Id
    private int id;
    private int playerId;
    private List<Card> cards;
    private double bet;

    public Hand() {}

    public Hand(int id, int playerId, List<Card> cards, double bet) {
        this.id = id;
        this.playerId = playerId;
        this.cards = cards;
        this.bet = bet;
    }


    public int getId() { return id; }

    public int getPlayerId() { return playerId; }

    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public List<Card> getCards() { return cards; }

    public void setCards(List<Card> cards) { this.cards = cards; }

    public double getBet() { return bet; }

    public void setBet(double bet) { this.bet = bet; }

    public Mono<Integer> calculateScore() {
        return Mono.fromCallable(() ->
                cards.stream().mapToInt(Card::getPoints).sum()
        ).flatMap(total -> {
            long numsOfAs = cards.stream()
                    .filter(card -> card.getValue().equals("AS"))
                    .count();

            if (total > 21 && numsOfAs > 0) {
                total -= 10;
            }

            return Mono.just(total);
        });
    }

    public Mono<Boolean> lostGame() {
        return calculateScore().map(score -> score > 21);
    }

    public Mono<Boolean> isBlackjack() {
        return calculateScore().map(score -> cards.size() == 2 && score == 21);
    }

    public Mono<Void> addCard(Card card) {
        this.cards.add(card);
        return Mono.empty();
    }


}

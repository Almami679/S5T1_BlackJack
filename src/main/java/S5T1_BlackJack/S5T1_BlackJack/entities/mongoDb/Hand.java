package S5T1_BlackJack.S5T1_BlackJack.entities.mongoDb;
import S5T1_BlackJack.S5T1_BlackJack.entities.Card;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

import java.util.List;

@Document(collection = "hands")
@Getter
@Setter
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

    public Mono<Boolean> checkBlackjack() {
        return calculateScore().map(score -> cards.size() == 2 && score == 21);
    }
    public Mono<Boolean> checkScoreHand() {
        return calculateScore().map(score -> score > 21);
    }

    public Mono<Void> addCard(Card card) {
        this.cards.add(card);
        return Mono.empty();
    }


}

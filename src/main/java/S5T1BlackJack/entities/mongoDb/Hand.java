package S5T1BlackJack.entities.mongoDb;
import S5T1BlackJack.entities.Card;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "hands")
@Getter
@Setter
@Schema(description = "Hand entity representing a player's cards")
public class Hand {


    @Schema(description = "List of cards in hand")
    private List<Card> cards = new ArrayList<>();

    public Hand() {}

    public Hand(List<Card> cards) {
        this.cards = cards;
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

    public Mono<Hand> addCard(Card card) {
        if (this.cards == null) {
            this.cards = new ArrayList<>();
        }
        this.cards.add(card);
        return Mono.just(this);
    }

}

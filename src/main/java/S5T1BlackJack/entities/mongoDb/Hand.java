package S5T1BlackJack.entities.mongoDb;
import S5T1BlackJack.entities.Card;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "hands")
@Getter
@Setter
@Schema(description = "Hand entity representing a player's cards")
public class Hand {

    @Id
    @Schema(description = "Hand ID", example = "60b8d295f1b2c93d2f1e7b9e")
    private String id;

    @Schema(description = "Player ID associated with this hand", example = "123456")
    private String playerId;

    @Schema(description = "List of cards in hand")
    private List<Card> cards = new ArrayList<>();

    public Hand() {}

    public Hand(String id, String playerId, List<Card> cards) {
        this.id = id;
        this.playerId = playerId;
        this.cards = (cards != null) ? cards : new ArrayList<>();
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

    public Mono<Void> addCard(Card card) {
        if (this.cards == null) {
            this.cards = new ArrayList<>();
        }
        this.cards.add(card);
        return Mono.empty();
    }
}

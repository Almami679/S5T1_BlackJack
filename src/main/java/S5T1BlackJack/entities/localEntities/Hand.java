package S5T1BlackJack.entities.localEntities;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Hand {


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

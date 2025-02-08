package S5T1BlackJack.entities;

import S5T1BlackJack.entities.enumsEntities.CardSuit;
import S5T1BlackJack.entities.enumsEntities.CardValue;
import S5T1BlackJack.exceptions.DeckIsEmptyException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                cards.add(new Card(suit, value));
            }
        }
        Collections.shuffle(cards);
    }

    public Mono<Card> nextCard() {
        return Mono.fromCallable(() -> {
            if (cards.isEmpty()) {
                throw new DeckIsEmptyException("No more cards in the deck");
            }
            Card nextCard = cards.get(0);
            cards.remove(0);
            return nextCard;
        });
    }

}


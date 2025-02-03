package S5T1_BlackJack.S5T1_BlackJack.entities;

import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.CardSuit;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.CardValue;
import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Card;
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
        return Mono.justOrEmpty(cards.isEmpty() ? null : cards.get(0));
    }

}


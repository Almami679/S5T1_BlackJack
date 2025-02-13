package S5T1BlackJack.entities.localEntities;


import S5T1BlackJack.entities.enumsEntities.CardSuit;
import S5T1BlackJack.entities.enumsEntities.CardValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    private int id;
    private CardSuit suit;
    private CardValue value;

    public Card(CardSuit suit, CardValue value){
        this.value = value;
        this.suit = suit;
    }

    public int getPoints(){
        return this.value.getPoints();
    }

    public String getValue() {
        return this.value.name();
    }
}






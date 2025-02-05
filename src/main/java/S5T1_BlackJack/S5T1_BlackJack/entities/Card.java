package S5T1_BlackJack.S5T1_BlackJack.entities;


import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.CardSuit;
import S5T1_BlackJack.S5T1_BlackJack.entities.enumsEntities.CardValue;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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






package S5T1BlackJack.entities.sql;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("players")
public class Player {

    @Id
    private Integer id;

    @Column("name")
    private String name;

    @Column("total_balance")
    private int totalBalance;

    @Column("score")
    private double score;



    public Player() {
    }

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.totalBalance = 10000;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }


    public double getScore() {
        return score;
    }

    public Player setScore(double score) {
        this.score = score;
        return this;
    }
}

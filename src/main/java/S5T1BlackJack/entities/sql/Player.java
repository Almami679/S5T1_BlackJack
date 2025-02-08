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

    @Getter
    @Column("total_balance")
    private int totalBalance;

    @Getter
    @Setter
    @Column("total_win_games")
    private int winGames;

    @Getter
    @Setter
    @Column("total_lost_games")
    private int lostGames;

    @Column("score")
    private double score;



    public Player() {
    }

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.totalBalance = 10000;
    }

    public Player (int id, String name, int totalBalance, double score, int totalWinGames, int totalLostGames ) {
        this.id = id;
        this.name = name;
        this.totalBalance = totalBalance;
        this.score = score;
        this.winGames = totalWinGames;
        this.lostGames = totalLostGames;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public int getScore() {
        return (int) score;
    }

    public void setScore() {
        int totalGames = winGames + lostGames;
        if (totalGames == 0) {
            score = 0;
        } else {
            score = Math.round(((double) winGames / totalGames) * 10000);
        }
    }

}

package S5T1BlackJack.entities.sql;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("players") // Nombre de la tabla en MySQL
public class Player {

    @Id
    private Integer id;

    @Column("name")
    private String name;

    @Column("games")
    private int games;

    @Column("win_games")
    private int winGames;

    @Column("score")
    private double score;

    @Column("total_balance")
    private int totalBalance;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
        this.games = 0;
        this.winGames = 0;
        this.score = 0;
        this.totalBalance = 10000;
    }

    public int getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(int balance) {
        this.totalBalance = balance;
    }

    public int getId() {
        return id;
    }

    public Player setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public int getGames() {
        return games;
    }

    public Player setGames(int games) {
        this.games = games;
        return this;
    }

    public int getWinGames() {
        return winGames;
    }

    public Player setWinGames(int winGames) {
        this.winGames = winGames;
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

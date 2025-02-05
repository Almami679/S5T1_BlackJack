package S5T1BlackJack.entities.enumsEntities;
public enum CardValue {
    AS(11),
    DOS(2),
    TRES(3),
    CUATRO(4),
    CINCO(5),
    SEIS(6),
    SIETE(7),
    OCHO(8),
    NUEVE(9),
    DIEZ(10),
    JOTA(10),
    REINA(10),
    REY(10);

    private final int points;

    CardValue(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}

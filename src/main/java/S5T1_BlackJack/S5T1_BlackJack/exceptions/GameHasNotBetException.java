package S5T1_BlackJack.S5T1_BlackJack.exceptions;

public class GameHasNotBetException extends RuntimeException {
    public GameHasNotBetException(String message) {
        super(message);
    }
}

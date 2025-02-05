package S5T1BlackJack.exceptions;

public class GameHasNotBetException extends RuntimeException {
    public GameHasNotBetException(String message) {
        super(message);
    }
}

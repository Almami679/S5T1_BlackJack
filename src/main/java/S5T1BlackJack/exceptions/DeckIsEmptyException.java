package S5T1BlackJack.exceptions;

public class DeckIsEmptyException extends RuntimeException {
    public DeckIsEmptyException(String message) {
        super(message);
    }
}

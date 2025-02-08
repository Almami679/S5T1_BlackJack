package S5T1BlackJack.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<String> handleNotFoundGameException(GameNotFoundException e) {
        log.error("Error due to non-existing entry in database: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GameHasFInishException.class)
    public ResponseEntity<String> handleGameHasFinishException(GameHasFInishException e) {
        log.error("Game has already finished: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GameHasNotBetException.class)
    public ResponseEntity<String> handleNotBetException(GameHasNotBetException e) {
        log.error("Error due to this game has not Bet: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ActionNotAvailableException.class)
    public ResponseEntity<String> handleActionNotAvailableException(ActionNotAvailableException e) {
        log.error("Action Error: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeckIsEmptyException.class)
    public ResponseEntity<String> handleDeckIsEmptyException(DeckIsEmptyException e) {
        log.error("The deck is empty: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicatedPlayerException.class)
    public ResponseEntity<String> handleDuplicatedPlayerException(DuplicatedPlayerException e) {
        log.error("Duplicated player error: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<String> handlePlayerNotFoundException(PlayerNotFoundException e) {
        log.error("Player not found: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerNameIsNullException.class)
    public ResponseEntity<String> handlePlayerNameIsNullException(PlayerNameIsNullException e) {
        log.error("Player name cannot be null: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Error due to designated value NULL: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage());
        return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


package S5T1_BlackJack.S5T1_BlackJack.DtoTestsValidations;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.entities.sql.Player;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class PlayerDtoTestsValidations {

    private Validator validator;
    public PlayerDtoTestsValidations() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    @DisplayName("TEST CREATE NEW PLAYER NAME FROM DTO - Invalid Empty Name")
    void testNamePlayerWithEmptyDTO() {
        PlayerDTO playerDto = new PlayerDTO("");

        Set<ConstraintViolation<PlayerDTO>> violations = validator.validate(playerDto);

        assertFalse(violations.isEmpty(), "Validation should fail for an empty name");
        assertEquals("name cannot be blank", violations.iterator().next().getMessage());
    }


    @Test
    @DisplayName("TEST CREATE NEW PLAYER NAME FROM DTO - Null Name")
    void testNamePlayerWithNullDTO() {
        PlayerDTO playerDto = new PlayerDTO(null);

        Set<ConstraintViolation<PlayerDTO>> violations = validator.validate(playerDto);

        assertFalse(violations.isEmpty(), "Validation should fail for null name");
        assertEquals("name cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("TEST CREATE NEW PLAYER NAME FROM DTO - Valid Name")
    void testNamePlayerWithValidDTO() {
        PlayerDTO playerDto = new PlayerDTO("Irenita");
        Player playerTest = new Player(playerDto.getName());

        Set<ConstraintViolation<PlayerDTO>> violations = validator.validate(playerDto);

        assertTrue(violations.isEmpty(), "valid name ");
        assertEquals(playerDto.getName(), playerTest.getName());
    }

    @Test
    @DisplayName("TEST CREATE NEW PLAYER NAME FROM DTO - Name Too Short")
    void testNamePlayerWithShortDTO() {
        PlayerDTO playerDto = new PlayerDTO("Al");

        Set<ConstraintViolation<PlayerDTO>> violations = validator.validate(playerDto);

        assertFalse(violations.isEmpty(), "Validation should fail for a short name");
        assertEquals("the name must be between 3 and 50 characters", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("TEST CREATE NEW PLAYER NAME FROM DTO - Name Too Long")
    void testNamePlayerWithLongDTO() {
        String nameToLong = "Putos tests de mierda locoooooo";
        PlayerDTO playerDto = new PlayerDTO("Putos tests de mierda locoooooooooooooo!!!!");

        Set<ConstraintViolation<PlayerDTO>> violations = validator.validate(playerDto);

        assertFalse(violations.isEmpty(), "Validation should fail for a long name");
        assertEquals("the name must be between 3 and 50 characters", violations.iterator().next().getMessage());
        assertTrue(nameToLong.length() > 30, "Name size: " + nameToLong.length());
    }
}

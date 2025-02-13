package S5T1_BlackJack.S5T1_BlackJack.DtoTestsValidations;

import S5T1BlackJack.DTO.AmountDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class AmountDtoTestValidations {

    private Validator validator;

    public AmountDtoTestValidations() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    @DisplayName("TEST CREATE NEW AMOUNT FROM DTO - Invalid short value")
    void testAmountWithInvalidShortValue() {
        int invalidAmount = 5;
        AmountDTO amountDto = new AmountDTO(invalidAmount);

        Set<ConstraintViolation<AmountDTO>> violations = validator.validate(amountDto);

        assertFalse(violations.isEmpty(), "Validation should fail for an amount less than 100");
        assertEquals("enter at least 100 tokens", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("TEST CREATE NEW AMOUNT FROM DTO - Invalid short value")
    void testAmountWithInvalidLargeValue() {
        int invalidAmount = 500000000;
        AmountDTO amountDto = new AmountDTO(invalidAmount);

        Set<ConstraintViolation<AmountDTO>> violations = validator.validate(amountDto);

        assertFalse(violations.isEmpty(), "Validation should fail for an amount less than 100");
        assertEquals("enter 50,000 tokens maximum", violations.iterator().next().getMessage());
    }
}

package S5T1BlackJack.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AmountDTO {

@NotNull(message = "amount cannot be null")
@Min(value = 100, message = "enter at least 100 tokens")
@Max(value = 50000, message = "enter 50,000 tokens maximum")
private int amount;

public int getAmount(){
    return amount;
}
}
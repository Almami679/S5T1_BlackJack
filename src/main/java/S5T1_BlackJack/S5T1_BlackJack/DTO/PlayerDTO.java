package S5T1_BlackJack.S5T1_BlackJack.DTO;

import S5T1_BlackJack.S5T1_BlackJack.exceptions.PlayerNameIsNullException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {

    @NotBlank(message = "name cannot be null")
    @Size(min = 3, max = 50, message = "the name must be between 3 and 50 characters")
    private String name;

    public String getName(){
        return name;
    }
}

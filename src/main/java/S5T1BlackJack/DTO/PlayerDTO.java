package S5T1BlackJack.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {

    @NotNull
    @NotBlank(message = "name cannot be null")
    @Size(min = 3, max = 50, message = "the name must be between 3 and 50 characters")
    private String name;

    public String getName(){
        return name;
    }
}

package S5T1BlackJack.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Player ranking information")
public class PlayerRankDTO {
    @Schema(description = "Player's name")
    private String name;

    @Schema(description = "Player's score")
    private int score;
}

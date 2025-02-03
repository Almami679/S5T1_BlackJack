package S5T1_BlackJack.S5T1_BlackJack.DTO;

import S5T1_BlackJack.S5T1_BlackJack.exceptions.PlayerNameIsNullException;

public record PlayerDTO (String name){
    public PlayerDTO {

        if (name == null || name.isEmpty()) {
            throw new PlayerNameIsNullException("Player name cannot be Null or empty");
        }
    }
}

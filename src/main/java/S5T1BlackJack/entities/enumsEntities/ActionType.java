package S5T1BlackJack.entities.enumsEntities;

import S5T1BlackJack.exceptions.ActionNotAvailableException;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public enum ActionType {
    HIT,
    STAND;

    @JsonCreator
    public static ActionType fromString(@Valid @NotNull String value) {
        for (ActionType action : ActionType.values()) {
            if (action.name().equalsIgnoreCase(value)) {
                return action;
            }
        }
        throw new ActionNotAvailableException("Invalid ActionType: " + value);
    }


}



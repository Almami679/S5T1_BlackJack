package S5T1_BlackJack.S5T1_BlackJack.service.playerService;

import S5T1_BlackJack.S5T1_BlackJack.DTO.PlayerDTO;
import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Player;

import java.util.List;

public interface PlayerServiceInteface {

    Player addPlayer(PlayerDTO playerDto);
//    Player updatePlayer(Player updatedPlayer);
    Player getPlayer(int id);
    Player getPlayerByName(PlayerDTO playerName);
    List<Player> getAllPlayers();
    boolean checkPlayer(PlayerDTO playerName);
}

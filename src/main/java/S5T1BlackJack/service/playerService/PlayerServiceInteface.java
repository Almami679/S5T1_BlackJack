package S5T1BlackJack.service.playerService;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.entities.sql.Player;

import java.util.List;

public interface PlayerServiceInteface {

    Player addPlayer(Player player);
//    Player updatePlayer(Player updatedPlayer);
    Player getPlayer(int id);
    Player getPlayerByName(PlayerDTO playerName);
    List<Player> getAllPlayers();
    boolean checkPlayer(PlayerDTO playerName);
}

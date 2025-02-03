package S5T1_BlackJack.S5T1_BlackJack.service;

import S5T1_BlackJack.S5T1_BlackJack.DTO.PlayerDTO;
import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Player;

import java.util.List;

public interface PlayerServiceInteface {

    Player addPlayer(PlayerDTO playerDto);
    Player updatePlayer(PlayerDTO updatedPlayer);
    void deletePlayer(int id);
    Player getPlayer(int id);
    List<Player> getAllPlayers();
}

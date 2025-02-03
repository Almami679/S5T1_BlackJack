package S5T1_BlackJack.S5T1_BlackJack.service;

import S5T1_BlackJack.S5T1_BlackJack.DTO.PlayerDTO;
import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Player;

import java.util.List;

public class PlayerService implements PlayerServiceInteface {


    @Override
    public Player addPlayer(PlayerDTO playerDto) {
        return null;
    }

    @Override
    public Player updatePlayer(PlayerDTO updatedPlayer) {
        return null;
    }

    @Override
    public void deletePlayer(int id) {

    }

    @Override
    public Player getPlayer(int id) {
        return null;
    }

    @Override
    public List<Player> getAllPlayers() {
        return List.of();
    }
}

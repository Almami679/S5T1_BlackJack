package S5T1BlackJack.service.playerService;

import S5T1BlackJack.DTO.PlayerDTO;
import S5T1BlackJack.entities.sql.Player;
import S5T1BlackJack.exceptions.DuplicatedPlayerException;
import S5T1BlackJack.exceptions.PlayerNotFoundException;
import S5T1BlackJack.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PlayerService implements PlayerServiceInteface {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player addPlayer(PlayerDTO playerDto) {
        Player player = new Player();
        player.setName(playerDto.getName());
        playerRepository.findPlayerByName(player.getName())
                .ifPresent(existingFruit -> {
                    throw new DuplicatedPlayerException("Entity with the name '" + player.getName() + "' already exists.");
                });

        return playerRepository.save(player);

    }

//    @Override
//    public Player updatePlayer(Player updatedPlayer) {
//        playerRepository.findById(updatedPlayer.getId())
//                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id " + updatedPlayer.getId()));
//        Player dbPlayer = playerRepository.getReferenceById(updatedPlayer.getId());
//        PlayerDTO validatedValues = new PlayerDTO(updatedPlayer.getName());
//        dbPlayer.setName(validatedValues.getName());
//
//        return playerRepository.save(dbPlayer);
//    }

    @Override
    public Player getPlayer(int id) {
        return null;
    }

    @Override
    public Player getPlayerByName(PlayerDTO playerName){
        return playerRepository.findPlayerByName(playerName.getName())
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));
    }

    public boolean checkPlayer(PlayerDTO playerName) {
        return getAllPlayers().stream().anyMatch(
                player -> player.getName().equalsIgnoreCase(playerName.getName()));
    }

    @Override
    public List<Player> getAllPlayers() {
        return List.of();
    }
}

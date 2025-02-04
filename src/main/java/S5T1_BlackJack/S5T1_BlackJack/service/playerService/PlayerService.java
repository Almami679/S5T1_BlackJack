package S5T1_BlackJack.S5T1_BlackJack.service.playerService;

import S5T1_BlackJack.S5T1_BlackJack.DTO.PlayerDTO;
import S5T1_BlackJack.S5T1_BlackJack.entities.sql.Player;
import S5T1_BlackJack.S5T1_BlackJack.exceptions.DuplicatedPlayerException;
import S5T1_BlackJack.S5T1_BlackJack.exceptions.PlayerNotFoundException;
import S5T1_BlackJack.S5T1_BlackJack.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PlayerService implements PlayerServiceInteface {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player addPlayer(PlayerDTO playerDto) {
        Player player = new Player();
        player.setName(playerDto.name());
        playerRepository.findPlayerByName(player.getName())
                .ifPresent(existingFruit -> {
                    throw new DuplicatedPlayerException("Entity with the name '" + player.getName() + "' already exists.");
                });

        return playerRepository.save(player);

    }

    @Override
    public Player updatePlayer(Player updatedPlayer) {
        Player dbPlayer = playerRepository.findById(updatedPlayer.getId())
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id " + updatedPlayer.getId()));

        PlayerDTO validatedValues = new PlayerDTO(updatedPlayer.getName());
        dbPlayer.setName(validatedValues.name());

        return playerRepository.save(dbPlayer);
    }

    @Override
    public Player getPlayer(int id) {
        return null;
    }

    @Override
    public Player getPlayerByName(PlayerDTO playerName){
        return playerRepository.findPlayerByName(playerName.name())
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));
    }

    public boolean checkPlayer(PlayerDTO playerName) {
        return getAllPlayers().contains(new
    }

    @Override
    public List<Player> getAllPlayers() {
        return List.of();
    }
}

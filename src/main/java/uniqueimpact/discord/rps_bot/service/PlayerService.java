package uniqueimpact.discord.rps_bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uniqueimpact.discord.rps_bot.entity.Player;
import uniqueimpact.discord.rps_bot.repository.PlayerRepository;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public Player find(String discordId) {
        return playerRepository.findById(discordId);
    }

    public Boolean exists(String discordId) {
        return playerRepository.existsById(discordId);
    }

    public void delete(Player player) {
        playerRepository.delete(player);
    }

    public Player createOrGet(String discordId) {
        if (exists(discordId)) {
            return find(discordId);
        } else {
            Player player = new Player();
            player.setId(discordId);
            return save(player);
        }
    }

}

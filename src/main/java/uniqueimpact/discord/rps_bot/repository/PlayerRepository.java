package uniqueimpact.discord.rps_bot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uniqueimpact.discord.rps_bot.entity.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {

    public Boolean existsById(String discordId);

    public Player findById(String discordId);

}

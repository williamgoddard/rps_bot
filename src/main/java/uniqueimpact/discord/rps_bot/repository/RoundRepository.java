package uniqueimpact.discord.rps_bot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uniqueimpact.discord.rps_bot.entity.Game;
import uniqueimpact.discord.rps_bot.entity.Round;

import java.util.List;

@Repository
public interface RoundRepository extends CrudRepository<Round, Integer> {
    Round findByGameAndRoundNum(Game game, Integer roundNumber);

    Round findByGameOrderByRoundNumDesc(Game game);

    List<Round> findAllByGameOrderByRoundNum(Game game);
}

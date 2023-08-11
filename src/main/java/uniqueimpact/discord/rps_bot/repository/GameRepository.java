package uniqueimpact.discord.rps_bot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uniqueimpact.discord.rps_bot.entity.Game;
import uniqueimpact.discord.rps_bot.util.GameStatus;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {

    public Boolean existsByPlayer1IdAndPlayer2IdAndStatus(String player1Id, String player2dId, GameStatus status);

    public Boolean existsByPlayer1IdAndStatus(String player1Id, GameStatus status);

    public Boolean existsByPlayer2IdAndStatus(String player2Id, GameStatus status);

    public Game findByPlayer1IdAndPlayer2IdAndStatusOrderByTimestampDesc(String player1Id, String player2Id, GameStatus status);

    Game findByPlayer1IdAndStatus(String player1Id, GameStatus status);

    Game findByPlayer2IdAndStatus(String player2Id, GameStatus status);

    public List<Game> findAllByPlayer1IdAndPlayer2IdAndStatusOrderByTimestampDesc(String player1Id, String player2Id, GameStatus status);

    List<Game> findAllByPlayer1IdAndStatus(String player1Id, GameStatus status);

    List<Game> findAllByPlayer2IdAndStatus(String player2Id, GameStatus status);
}

package uniqueimpact.discord.rps_bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uniqueimpact.discord.rps_bot.entity.Game;
import uniqueimpact.discord.rps_bot.entity.Player;
import uniqueimpact.discord.rps_bot.repository.GameRepository;
import uniqueimpact.discord.rps_bot.util.GameStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public Game find(Player challenger, Player challenged, GameStatus status) {

        if (challenger == null && challenged == null) {
            return null;
        }

        if (challenger == null) {
            return gameRepository.findByPlayer2IdAndStatus(challenged.getId(), status);
        }

        if (challenged == null) {
            return gameRepository.findByPlayer1IdAndStatus(challenger.getId(), status);
        }

        return gameRepository.findByPlayer1IdAndPlayer2IdAndStatusOrderByTimestampDesc(challenger.getId(), challenged.getId(), status);
    }

    public Boolean exists(Player challenger, Player challenged, GameStatus status) {

        if (challenger == null && challenged == null) {
            return false;
        }

        if (challenger == null) {
            return gameRepository.existsByPlayer2IdAndStatus(challenged.getId(), status);
        }

        if (challenged == null) {
            return gameRepository.existsByPlayer1IdAndStatus(challenger.getId(), status);
        }

        return gameRepository.existsByPlayer1IdAndPlayer2IdAndStatus(challenger.getId(), challenged.getId(), status);

    }

    public void delete(Game game) {
        gameRepository.delete(game);
    }

    public List<Game> findAll(Player challenger, Player challenged, GameStatus status) {

        if (challenger == null && challenged == null) {
            return new ArrayList<>();
        }

        if (challenger == null) {
            return gameRepository.findAllByPlayer2IdAndStatus(challenged.getId(), status);
        }

        if (challenged == null) {
            return gameRepository.findAllByPlayer1IdAndStatus(challenger.getId(), status);
        }

        return gameRepository.findAllByPlayer1IdAndPlayer2IdAndStatusOrderByTimestampDesc(challenger.getId(), challenged.getId(), status);

    }
}

package uniqueimpact.discord.rps_bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uniqueimpact.discord.rps_bot.entity.Game;
import uniqueimpact.discord.rps_bot.entity.Round;
import uniqueimpact.discord.rps_bot.repository.RoundRepository;

import java.util.List;

@Service
public class RoundService {

    @Autowired
    private RoundRepository roundRepository;

    public Round save(Round round) {
        return roundRepository.save(round);
    }

    public void delete(Round round) {
        roundRepository.delete(round);
    }

    public Round find(Game game, Integer roundNumber) {
        return roundRepository.findByGameAndRoundNum(game, roundNumber);
    }

    public List<Round> findAll(Game game) {
        return roundRepository.findAllByGameOrderByRoundNum(game);
    }

    public Round findMostRecent(Game game) {
        return roundRepository.findByGameOrderByRoundNumDesc(game);
    }

}

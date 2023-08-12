package uniqueimpact.discord.rps_bot.discord.buttons;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uniqueimpact.discord.rps_bot.discord.senders.RpsSender;
import uniqueimpact.discord.rps_bot.discord.utils.WebhookStore;
import uniqueimpact.discord.rps_bot.entity.Game;
import uniqueimpact.discord.rps_bot.entity.Round;
import uniqueimpact.discord.rps_bot.service.GameService;
import uniqueimpact.discord.rps_bot.service.PlayerService;
import uniqueimpact.discord.rps_bot.service.RoundService;
import uniqueimpact.discord.rps_bot.util.GameStatus;
import uniqueimpact.discord.rps_bot.util.RoundOutcome;
import uniqueimpact.discord.rps_bot.util.Selection;

@Service
public class RpsButton implements Button {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @Autowired
    private RoundService roundService;

    @Autowired
    private RpsSender roundSender;

    @Override
    public void run(ButtonInteractionEvent button, String[] buttonArgs) {

        button.deferReply().setEphemeral(true).queue();
        InteractionHook hook = button.getHook();

        String choice = buttonArgs[1];
        Integer gameId = Integer.parseInt(buttonArgs[2]);
        Integer roundNum = Integer.parseInt(buttonArgs[3]);
        String playerNum = buttonArgs[4];

        Game game = gameService.find(gameId);

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            hook.sendMessage("This game is no longer in progress.").setEphemeral(true).queue();
            return;
        }

        Round round = roundService.find(game, roundNum);

        if (round.getOutcome() != RoundOutcome.UNDECIDED) {
            hook.sendMessage("This round is no longer in progress.").setEphemeral(true).queue();
            return;
        }

        if ((playerNum.equals("1") && round.getPlayer1Choice() != Selection.UNDECIDED) || (playerNum.equals("2") && round.getPlayer2Choice() != Selection.UNDECIDED)) {
            hook.sendMessage("You have already made your choice for this round.").setEphemeral(true).queue();
            return;
        }

        Selection selection;
        switch (choice) {
            case "R" -> selection = Selection.ROCK;
            case "P" -> selection = Selection.PAPER;
            case "S" -> selection = Selection.SCISSORS;
            case "F" -> selection = Selection.FORFEIT;
            default -> {
                hook.sendMessage("Error: Invalid choice (" + choice + ")").setEphemeral(true).queue();
                return;
            }
        }

        if (playerNum.equals("1")) {
            round.setPlayer1Choice(selection);
        } else {
            round.setPlayer2Choice(selection);
        }

        round = roundService.save(round);

        WebhookStore.setWebhook(game.getId() + ":" + playerNum, hook);

        if (round.getOutcome() == RoundOutcome.UNDECIDED) {
            return;
        }

        switch (round.getOutcome()) {
            case PLAYER_1_WIN -> game.setPlayer1Score(game.getPlayer1Score() + 1);
            case PLAYER_2_WIN -> game.setPlayer2Score(game.getPlayer2Score() + 1);
        }

        JDA jda = button.getJDA();

        roundSender.sendRoundOutcome(jda, game, round);

        if (round.getPlayer2Choice() == Selection.FORFEIT && round.getPlayer1Choice() == Selection.FORFEIT) {
            game.setStatus(GameStatus.TIE);
        } else if (round.getPlayer1Choice() == Selection.FORFEIT) {
            game.setStatus(GameStatus.PLAYER_2_WIN);
        } else if (round.getPlayer2Choice() == Selection.FORFEIT) {
            game.setStatus(GameStatus.PLAYER_1_WIN);
        } else if (game.getPlayer1Score() >= game.getFirstTo()) {
            game.setStatus(GameStatus.PLAYER_1_WIN);
        } else if (game.getPlayer2Score() >= game.getFirstTo()) {
            game.setStatus(GameStatus.PLAYER_2_WIN);
        }

        game.setRoundNum(game.getRoundNum() + 1);

        game = gameService.save(game);

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            roundSender.sendGameOutcome(jda, game);
            WebhookStore.removeWebhook(game.getId() + ":1");
            WebhookStore.removeWebhook(game.getId() + ":2");
            return;
        }

        Round next_round = new Round();
        next_round.setGame(game);
        next_round.setRoundNum(game.getRoundNum());
        next_round.setPlayer1Choice(Selection.UNDECIDED);
        next_round.setPlayer2Choice(Selection.UNDECIDED);

        roundService.save(next_round);

        roundSender.sendNextRound(game);

    }

}

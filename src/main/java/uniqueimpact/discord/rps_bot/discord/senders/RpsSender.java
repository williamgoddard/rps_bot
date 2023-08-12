package uniqueimpact.discord.rps_bot.discord.senders;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uniqueimpact.discord.rps_bot.discord.utils.WebhookStore;
import uniqueimpact.discord.rps_bot.entity.Game;
import uniqueimpact.discord.rps_bot.entity.Round;
import uniqueimpact.discord.rps_bot.service.RoundService;

import java.util.List;

@Service
public class RpsSender {

    @Autowired
    private RoundService roundService;

    public void sendNextRound(Game game) {

        InteractionHook player1_hook = WebhookStore.getWebhook(game.getId() + ":1");
        InteractionHook player2_hook = WebhookStore.getWebhook(game.getId() + ":2");

        MessageCreateData player1_message = new MessageCreateBuilder()
                .setContent("Rock Paper Scissors!")
                .addComponents(
                        ActionRow.of(
                                Button.of(ButtonStyle.SECONDARY, "RPS R " + game.getId() + " " + game.getRoundNum() + " 1", "Rock").withEmoji(Emoji.fromUnicode("U+1FAA8")),
                                Button.of(ButtonStyle.SECONDARY, "RPS P " + game.getId() + " " + game.getRoundNum() + " 1", "Paper").withEmoji(Emoji.fromUnicode("U+1F4C4")),
                                Button.of(ButtonStyle.SECONDARY, "RPS S " + game.getId() + " " + game.getRoundNum() + " 1", "Scissors").withEmoji(Emoji.fromUnicode("U+2702")),
                                Button.of(ButtonStyle.DANGER, "RPS F " + game.getId() + " " + game.getRoundNum() + " 1", "Forfeit")
                        )
                )
                .build();

        MessageCreateData player2_message = new MessageCreateBuilder()
                .setContent("Rock Paper Scissors!")
                .addComponents(
                        ActionRow.of(
                                Button.of(ButtonStyle.SECONDARY, "RPS R " + game.getId() + " " + game.getRoundNum() + " 2", "Rock").withEmoji(Emoji.fromUnicode("U+1FAA8")),
                                Button.of(ButtonStyle.SECONDARY, "RPS P " + game.getId() + " " + game.getRoundNum() + " 2", "Paper").withEmoji(Emoji.fromUnicode("U+1F4C4")),
                                Button.of(ButtonStyle.SECONDARY, "RPS S " + game.getId() + " " + game.getRoundNum() + " 2", "Scissors").withEmoji(Emoji.fromUnicode("U+2702")),
                                Button.of(ButtonStyle.DANGER, "RPS F " + game.getId() + " " + game.getRoundNum() + " 2", "Forfeit")
                        )
                )
                .build();

        player1_hook.sendMessage(player1_message).setEphemeral(true).queue();
        player2_hook.sendMessage(player2_message).setEphemeral(true).queue();

    }

    public void sendRoundOutcome(JDA jda, Game game, Round round) {

        String player1Tag = "<@" + game.getPlayer1().getId() + ">";
        String player2Tag = "<@" + game.getPlayer2().getId() + ">";

        String resultMessage = "> " + player1Tag + " vs. " + player2Tag + " - " + "Round " + round.getRoundNum() + " results:\n" +
                getRoundResultLine(round, player1Tag, player2Tag);

        jda.getTextChannelById(game.getPlayer1Channel()).sendMessage(resultMessage).queue();
        if (!game.getPlayer1Channel().equals(game.getPlayer2Channel())) {
            jda.getTextChannelById(game.getPlayer2Channel()).sendMessage(resultMessage).queue();
        }

    }

    public void sendGameOutcome(JDA jda, Game game) {

        InteractionHook player1Hook = WebhookStore.getWebhook(game.getId() + ":1");
        InteractionHook player2Hook = WebhookStore.getWebhook(game.getId() + ":2");

        String player1Tag = "<@" + game.getPlayer1().getId() + ">";
        String player2Tag = "<@" + game.getPlayer2().getId() + ">";

        List<Round> rounds = roundService.findAll(game);

        String finalMessage = "> " + player1Tag + " vs. " + player2Tag + " - " + "Game results:\n";

        for (Round round : rounds) {
            finalMessage += getRoundResultLine(round, player1Tag, player2Tag);
        }

        switch (game.getStatus()) {
            case PLAYER_1_WIN -> finalMessage += player1Tag + " wins!";
            case PLAYER_2_WIN -> finalMessage += player2Tag + " wins!";
            case TIE -> finalMessage += "It's a tie!";
            default -> finalMessage += "Something went wrong...";
        }

        jda.getTextChannelById(game.getPlayer1Channel()).sendMessage(finalMessage).queue();
        if (!game.getPlayer1Channel().equals(game.getPlayer2Channel())) {
            jda.getTextChannelById(game.getPlayer2Channel()).sendMessage(finalMessage).queue();
        }

        WebhookStore.getWebhook(game.getId() + ":1").sendMessage("Game over!").queue();
        WebhookStore.getWebhook(game.getId() + ":2").sendMessage("Game over!").queue();

    }

    private String getRoundResultLine(Round round, String player1Tag, String player2Tag) {

        String player1Emoji;
        switch (round.getPlayer1Choice()) {
            case ROCK -> player1Emoji = ":rock:";
            case PAPER -> player1Emoji = ":page_facing_up:";
            case SCISSORS -> player1Emoji = ":scissors:";
            case FORFEIT -> player1Emoji = ":x:";
            default -> player1Emoji = ":question:";
        }

        String player2Emoji;
        switch (round.getPlayer2Choice()) {
            case ROCK -> player2Emoji = ":rock:";
            case PAPER -> player2Emoji = ":page_facing_up:";
            case SCISSORS -> player2Emoji = ":scissors:";
            case FORFEIT -> player2Emoji = ":x:";
            default -> player2Emoji = ":question:";
        }

        String resultEmoji;
        switch (round.getOutcome()) {
            case PLAYER_1_WIN -> resultEmoji = ":arrow_right:";
            case PLAYER_2_WIN -> resultEmoji = ":arrow_left:";
            case TIE -> resultEmoji = ":left_right_arrow:";
            default -> resultEmoji = ":question:";
        }

        return "> " + player1Tag + " " + player1Emoji + " " + resultEmoji + " " + player2Emoji + " " + player2Tag + "\n";

    }

}

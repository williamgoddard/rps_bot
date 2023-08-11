package uniqueimpact.discord.rps_bot.discord.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uniqueimpact.discord.rps_bot.entity.Game;
import uniqueimpact.discord.rps_bot.entity.Player;
import uniqueimpact.discord.rps_bot.service.GameService;
import uniqueimpact.discord.rps_bot.service.PlayerService;
import uniqueimpact.discord.rps_bot.util.GameStatus;

import java.util.List;

@Service
public class RpsCommand implements Command {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @Override
    public void run(SlashCommandInteractionEvent command) {

        String path = command.getFullCommandName();

        switch (path) {
            case "rps challenge" -> challenge(command);
            case "rps accept" -> accept(command);
            default -> command.reply("Error: Invalid command path (" + path + ")").setEphemeral(true).queue();
        }

    }

    private void challenge(SlashCommandInteractionEvent command) {

        command.deferReply().setEphemeral(true).queue();
        InteractionHook hook = command.getHook();

        User challenger_user = command.getUser();
        User challenged_user = command.getOption("user").getAsUser();
        Integer firstTo = command.getOption("first-to", 1, OptionMapping::getAsInt);

        if (challenged_user.isBot()) {
            hook.sendMessage("You cannot challenge a bot.").setEphemeral(true).queue();
            return;
        }

        if (challenger_user.getId().equals(challenged_user.getId())) {
            hook.sendMessage("You cannot challenge yourself.").setEphemeral(true).queue();
            return;
        }

        Player challenger_player = playerService.createOrGet(challenger_user.getId());
        Player challenged_player = playerService.createOrGet(challenged_user.getId());

        if (gameService.exists(challenger_player, challenged_player, GameStatus.PENDING)) {
            hook.sendMessage("You are already challenging " + challenged_user.getAsMention() + ". Please wait for them to respond.").setEphemeral(true).queue();
            return;
        }

        if (gameService.exists(challenger_player, challenged_player, GameStatus.IN_PROGRESS) || gameService.exists(challenged_player, challenger_player, GameStatus.IN_PROGRESS)) {
            hook.sendMessage("You are already in a game with " + challenged_user.getAsMention() + ".").setEphemeral(true).queue();
            return;
        }

        TextChannel channel = command.getChannel().asTextChannel();

        Game game = new Game();
        game.setPlayer1(challenger_player);
        game.setPlayer2(challenged_player);
        game.setPlayer1Channel(channel.getId());
        game.setPlayer2Channel(channel.getId());
        game.setTimestamp(System.currentTimeMillis());
        game.setFirstTo(firstTo);
        game.setPlayer1Score(0);
        game.setPlayer2Score(0);
        game.setStatus(GameStatus.PENDING);

        gameService.save(game);

        channel.sendMessage(challenger_user.getAsMention() + " challenged " + challenged_user.getAsMention() + " to a game of Rock Paper Scissors!\nUse `/rps accept` to accept the challenge!").queue();

    }

    private void accept(SlashCommandInteractionEvent command) {

        command.deferReply().setEphemeral(true).queue();
        InteractionHook hook = command.getHook();

        User challenged_user = command.getUser();
        User challenger_user = command.getOption("user", null, OptionMapping::getAsUser);

        Game game;

        if (challenger_user != null) {

            if (challenger_user.isBot()) {
                hook.sendMessage("You cannot accept a challenge from a bot.").setEphemeral(true).queue();
                return;
            }

            if (challenged_user.getId().equals(challenger_user.getId())) {
                hook.sendMessage("You cannot accept a challenge from yourself.").setEphemeral(true).queue();
                return;
            }

            Player challenged_player = playerService.createOrGet(challenged_user.getId());
            Player challenger_player = playerService.createOrGet(challenger_user.getId());

            if (!gameService.exists(challenger_player, challenged_player, GameStatus.PENDING)) {
                hook.sendMessage("You do not have a pending challenge from " + challenger_user.getAsMention() + ".").setEphemeral(true).queue();
                return;
            }

            game = gameService.find(challenger_player, challenged_player, GameStatus.PENDING);

        } else {

            Player challenged_player = playerService.createOrGet(challenged_user.getId());

            List<Game> challenges = gameService.findAll(null, challenged_player, GameStatus.PENDING);

            if (challenges.size() == 0) {
                hook.sendMessage("You do not have any pending challenges.").setEphemeral(true).queue();
                return;
            }

            if (challenges.size() > 1) {
                hook.sendMessage("You have multiple pending challenges. Please specify which one you would like to accept.").setEphemeral(true).queue();
                return;
            }

            game = challenges.get(0);

        }

        TextChannel channel = command.getChannel().asTextChannel();

        if (!channel.getId().equals(game.getPlayer2Channel())) {
            game.setPlayer2Channel(channel.getId());
        }

        game.setStatus(GameStatus.IN_PROGRESS);

        gameService.save(game);

    }

}

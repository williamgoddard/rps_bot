package uniqueimpact.discord.rps_bot.discord.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

public class CommandSetup {

    public static void addCommands(JDA bot) {

        List<CommandData> commands = new ArrayList<>();

        commands.add(Commands.slash("rps", "Rock Paper Scissors").addSubcommands(
                new SubcommandData("challenge", "Challenge a friend to a round of Rock Paper Scissors!").addOptions(
                        new OptionData(OptionType.USER, "user", "The user you want to challenge.")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "first-to", "The score to win. (Default: 1)")
                                .setRequired(false)
                                .setRequiredRange(1, 10)
                ),
                new SubcommandData("accept", "Accept a Rock Paper Scissors challenge!").addOptions(
                        new OptionData(OptionType.USER, "user", "The user you want accept the challenge of.")
                                .setRequired(false)
                )
        ));

        //bot.updateCommands().addCommands().addCommands(commands).queue();
        bot.getGuildById("933432644857909339").updateCommands().addCommands(commands).queue();

    }

}

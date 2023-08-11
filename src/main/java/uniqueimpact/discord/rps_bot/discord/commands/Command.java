package uniqueimpact.discord.rps_bot.discord.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface Command {

    public void run(SlashCommandInteractionEvent command);

}

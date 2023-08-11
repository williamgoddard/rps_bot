package uniqueimpact.discord.rps_bot.discord.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import uniqueimpact.discord.rps_bot.discord.commands.CommandHandler;

@Service
public class CommandListener implements EventListener{

    private final CommandHandler commandController;

    public CommandListener(CommandHandler commandController) {
        this.commandController = commandController;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {

        if (!(event instanceof SlashCommandInteractionEvent)) {
            return;
        }

        SlashCommandInteractionEvent commandEvent = (SlashCommandInteractionEvent) event;

        commandController.handle(commandEvent);

    }

}

package uniqueimpact.discord.rps_bot.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uniqueimpact.discord.rps_bot.discord.listeners.ButtonListener;
import uniqueimpact.discord.rps_bot.discord.listeners.CommandListener;
import uniqueimpact.discord.rps_bot.discord.listeners.MessageListener;
import uniqueimpact.discord.rps_bot.discord.listeners.ReadyListener;
import uniqueimpact.discord.rps_bot.discord.utils.CommandSetup;

@Component
public class DiscordRunner implements CommandLineRunner {

    private final MessageListener messageListener;
    private final ReadyListener readyListener;
    private final CommandListener commandListener;
    private final ButtonListener buttonListener;

    @Value("${discord.bot-token}")
    private String botToken;

    public DiscordRunner(MessageListener messageListener, ReadyListener readyListener, CommandListener commandListener, ButtonListener buttonListener) {
        this.messageListener = messageListener;
        this.readyListener = readyListener;
        this.commandListener = commandListener;
        this.buttonListener = buttonListener;
    }

    @Override
    public void run(String... args) throws InterruptedException {

        // Create the builder
        JDABuilder builder = JDABuilder.createDefault(botToken)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT);

        // Add event listeners to the builder
        builder.addEventListeners(
                messageListener,
                readyListener,
                commandListener,
                buttonListener
        );

        // Create and start the bot
        JDA bot = builder.build();
        bot.awaitReady();

        // Set up the commands
        CommandSetup.addCommands(bot);

    }

}
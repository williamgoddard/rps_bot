package uniqueimpact.discord.rps_bot.discord.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class MessageListener implements EventListener {

	@Override
	public void onEvent(@NotNull GenericEvent event) {

		if (!(event instanceof MessageReceivedEvent)) {
			return;
		}

		MessageReceivedEvent messageEvent = (MessageReceivedEvent) event;
		if (messageEvent.getAuthor().isBot()) {
			return;
		}

	}
}

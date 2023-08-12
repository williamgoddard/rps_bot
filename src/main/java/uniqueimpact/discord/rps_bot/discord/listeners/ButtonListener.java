package uniqueimpact.discord.rps_bot.discord.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.stereotype.Service;
import uniqueimpact.discord.rps_bot.discord.buttons.ButtonHandler;

@Service
public class ButtonListener implements EventListener {

    private final ButtonHandler buttonHandler;

    public ButtonListener(ButtonHandler buttonHandler) {
        this.buttonHandler = buttonHandler;
    }

    @Override
    public void onEvent(GenericEvent event) {

        if (!(event instanceof ButtonInteractionEvent)) {
            return;
        }

        ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;

        buttonHandler.handle(buttonEvent);

    }

}

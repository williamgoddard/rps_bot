package uniqueimpact.discord.rps_bot.discord.utils;

import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.HashMap;
import java.util.Map;

public class WebhookStore {

    private static final Map<String, InteractionHook> webhooks = new HashMap<>();

    public static InteractionHook getWebhook(String id) {
        return webhooks.get(id);
    }

    public static void setWebhook(String id, InteractionHook webhook) {
        webhooks.put(id, webhook);
    }

    public static void removeWebhook(String id) {
        webhooks.remove(id);
    }

}

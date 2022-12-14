package io.thorasine.scrappybot.message;

import javax.annotation.PostConstruct;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.integration.BotFrameworkHttpAdapter;
import com.microsoft.bot.integration.Configuration;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.ConversationReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final Configuration configuration;
    private final BotFrameworkHttpAdapter adapter;
    private final ConversationReference kavoszDailyConversation;
    private String APP_ID;

    @PostConstruct
    private void init() {
        APP_ID = configuration.getProperty("MicrosoftAppId");
    }

    public void sendMessage(TurnContext turnContext, String message) {
        sendMessage(turnContext, MessageFactory.text(message));
    }

    public void sendMessage(TurnContext turnContext, Activity activity) {
        turnContext.sendActivity(activity);
    }

    public void sendGroupMessage(String message) {
        sendGroupMessage(MessageFactory.text(message));
    }

    public void sendGroupMessage(Activity activity) {
        sendMessage(kavoszDailyConversation, activity);
    }

    public void deleteMessage(TurnContext turnContext) {
        turnContext.deleteActivity(turnContext.getActivity().getReplyToId());
    }

    private void sendMessage(ConversationReference reference, Activity activity) {
        adapter.continueConversation(APP_ID, reference, turnContext -> turnContext.sendActivity(activity).thenApply(resourceResponse -> null));
    }
}

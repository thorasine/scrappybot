package io.thorasine.scrappybot.activity;

import com.microsoft.bot.integration.BotFrameworkHttpAdapter;
import com.microsoft.bot.schema.ConversationReference;
import io.thorasine.scrappybot.properties.MicrosoftAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProactiveMessageService {

    private final BotFrameworkHttpAdapter adapter;
    private final ConversationReferences conversationReferences;
    private final MicrosoftAuthProperties microsoftAuthProperties;

    @GetMapping("/api/notify")
    public void proactiveMessage() {
        for (ConversationReference reference : conversationReferences.values()) {
            adapter.continueConversation(microsoftAuthProperties.getAppId(), reference, turnContext -> turnContext.sendActivity("proactive hello").thenApply(resourceResponse -> null));
        }
    }
}

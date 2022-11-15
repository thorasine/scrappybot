package io.thorasine.scrappybot.activity;

import com.microsoft.bot.schema.ChannelAccount;
import com.microsoft.bot.schema.ConversationAccount;
import com.microsoft.bot.schema.ConversationReference;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConversationBeans {

    @Bean
    public ConversationReference kavoszDailyConversation() {
        ConversationReference conversationReference = new ConversationReference();
        ChannelAccount bot = new ChannelAccount();
        bot.setId("28:fe8b029c-89bc-48dd-b815-ece93e9fba4a");
        conversationReference.setBot(bot);
        ConversationAccount conversation = new ConversationAccount();
        conversation.setIsGroup(true);
        conversation.setId("19:42c68661c3ee4fa899ece84879102c10@thread.v2");
        conversationReference.setConversation(conversation);
        conversationReference.setServiceUrl("https://smba.trafficmanager.net/emea/");
        return conversationReference;
    }
}

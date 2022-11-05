package io.thorasine.scrappybot.activity;

import java.util.concurrent.ConcurrentHashMap;

import com.microsoft.bot.schema.ConversationReference;
import org.springframework.stereotype.Component;

@Component
public class ConversationReferences extends ConcurrentHashMap<String, ConversationReference> {
}

package io.thorasine.scrappybot.features.help.service;

import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.features.common.service.CommandLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelpService {

    private final CommandLineService commandLineService;

    public CompletableFuture<Void> getAllCommandsHelp(TurnContext turnContext) {
        String allCommandsHelpMessage = commandLineService.getAllCommandsHelp();
        return turnContext.sendActivity(MessageFactory.text(allCommandsHelpMessage)).thenApply(sendResult -> null);
    }
}

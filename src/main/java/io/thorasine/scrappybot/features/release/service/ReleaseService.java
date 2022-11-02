package io.thorasine.scrappybot.features.release.service;

import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReleaseService {

    public CompletableFuture<Void> release(TurnContext turnContext, CommandLine args) {
        String branch = args.getOptionValue("branch");
        String tag = args.getOptionValue("tag");
        if (args.hasOption("abort")) {
            //abort
        }
        return turnContext.sendActivity(MessageFactory.text("WIP - not implemented")).thenApply(sendResult -> null);
    }
}

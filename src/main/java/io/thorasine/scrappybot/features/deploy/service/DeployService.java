package io.thorasine.scrappybot.features.deploy.service;

import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.features.common.enums.Command;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployService {

    public CompletableFuture<Void> deploy(TurnContext turnContext, Command command, CommandLine args) {
        String branch;
        String tag;
        if (args.getArgList().isEmpty()) {
            //todo return card with clickable buttons [develop], [12.0.0], [abort], [cancel] etc
        }
        if (args.hasOption("abort")) {
            //abort
        }
        if (args.hasOption("branch")) {
            branch = args.getOptionValue("branch");
        }
        if (args.hasOption("tag")) {
            tag = args.getOptionValue("tag");
        }
        return turnContext.sendActivity(MessageFactory.text("WIP - not implemented")).thenApply(sendResult -> null);
    }
}

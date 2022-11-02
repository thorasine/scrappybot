package io.thorasine.scrappybot.features.help.service;

import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.features.common.enums.Command;
import io.thorasine.scrappybot.features.common.service.CommandLineService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelpService {

    private final CommandLineService commandLineService;

    public CompletableFuture<Void> getAllCommandsHelp(TurnContext turnContext, CommandLine args) {
        String message;
        if (args.hasOption("command")) {
            Command command = Command.from(args.getOptionValue("command"));
            if (null != command) {
                message = commandLineService.getCommandHelp(command, true);
            } else {
                message = "Command " + '"' + args.getOptionValue("command") + '"' + " not found.";
            }
        } else {
            message = commandLineService.getAllCommandsHelp(true);
        }
        return turnContext.sendActivity(MessageFactory.text(message)).thenApply(sendResult -> null);
    }
}

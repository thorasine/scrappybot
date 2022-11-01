package io.thorasine.scrappybot.features.release.service;

import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.features.common.enums.Command;
import io.thorasine.scrappybot.features.common.service.CommandLineService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReleaseService {

    private final CommandLineParser parser;
    private final CommandLineService commandLineService;

    public CompletableFuture<Void> release(TurnContext turnContext, Command command, String[] args) {
        CommandLine commandLineArgs;
        try {
            commandLineArgs = getCommandLineArgs(command, args);
        } catch (ParseException exception) {
            exception.printStackTrace();
            return commandLineService.getCommandErrorHelpMessage(turnContext, exception, command);
        }
        return null;
    }

    private CommandLine getCommandLineArgs(Command command, String[] args) throws ParseException {
        return parser.parse(command.getOptions(), args);
    }
}

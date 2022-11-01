package io.thorasine.scrappybot.features.common.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.features.common.enums.Command;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandLineService {

    private final HelpFormatter helpFormatter;
    private final String EMPTY_LINE = "\u206E\n";
    private final String LINE_BREAK = "---\n";

    public CompletableFuture<Void> getCommandErrorHelpMessage(TurnContext turnContext, ParseException exception, Command command) {
        String exceptionMessage = exception.getMessage() + "\n";
        String helpMenu = getHelpMenu(command);
        return turnContext.sendActivity(MessageFactory.text(exceptionMessage + helpMenu)).thenApply(sendResult -> null);
    }

    public String getAllCommandsHelp() {
        StringBuilder message = new StringBuilder();
        message.append("# All commands\n");
        message.append(LINE_BREAK);
        for (Command command : Command.values()) {
            if (command == Command.HELP) {
                continue;
            }
            message.append(getHelpMenu(command));
            message.append(EMPTY_LINE);
        }
        message.setLength(message.length() - 2);
        return removeUsage(message.toString());
    }

    private String getHelpMenu(Command command) {
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        helpFormatter.printHelp(pw, 80, command.getValue(), "", command.getOptions(),
            helpFormatter.getLeftPadding(), helpFormatter.getDescPadding(), "", true);
        pw.flush();
        return formatHelpMenuMessage(out.toString());
    }

    private String formatHelpMenuMessage(String response) {
        String[] splits = response.split("\n");
        StringBuilder message = new StringBuilder();
        for (String split : splits) {
            if (!split.startsWith(" -")) {
                message.append("## ").append(split).append("\n");
            } else {
                message.append(split).append("\n");
            }
        }
        response = message.toString();
        response = response.replace("\n", "\n\n");
        response = response.replace("<arg>", "\\<arg\\>");
        return response;
    }

    private String removeUsage(String message) {
        return message.replace("usage: ", "");
    }
}

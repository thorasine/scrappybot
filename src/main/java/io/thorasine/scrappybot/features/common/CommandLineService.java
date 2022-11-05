package io.thorasine.scrappybot.features.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.features.common.enums.Command;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
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

    public String getAllCommandsHelp(boolean withArgs) {
        StringBuilder message = new StringBuilder();
        for (Command command : Command.values()) {
            message.append(getCommandHelp(command, withArgs));
            message.append(EMPTY_LINE);
        }
        message.setLength(message.length() - 2);
        return message.toString();
    }

    public String getCommandHelp(Command command, boolean withArgs) {
        StringBuilder message = new StringBuilder();
        message.append("# ").append(StringUtils.capitalize(command.getValue())).append("\n");
        message.append(command.getDescription()).append("\n");
        message.append(getHelpMenu(command, withArgs));
        return message.toString();
    }

    private String getHelpMenu(Command command) {
        return getHelpMenu(command, true);
    }

    private String getHelpMenu(Command command, boolean withArgs) {
        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        helpFormatter.printHelp(pw, 80, command.getValue(), "", command.getOptions(),
            helpFormatter.getLeftPadding(), helpFormatter.getDescPadding(), "", true);
        pw.flush();
        return formatHelpMenuMessage(out.toString(), withArgs);
    }

    private String formatHelpMenuMessage(String response, boolean withArgs) {
        String[] splits = response.split("\n");
        StringBuilder message = new StringBuilder();
        for (String split : splits) {
            if (!split.startsWith(" -")) {
                message.append("## ").append(split).append("\n");
            } else if (withArgs) {
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

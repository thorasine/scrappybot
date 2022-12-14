package io.thorasine.scrappybot.command.commandline;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.command.Command;
import io.thorasine.scrappybot.techcore.error.SystemRuntimeErrorException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandLineService {

    private final CommandLineParser commandLineParser;
    private final HelpFormatter helpFormatter;
    private final String EMPTY_LINE = "\u206E\n";
    private final String LINE_BREAK = "---\n";

    public Command getCommand(TurnContext turnContext, String[] stringArgs) {
        Command command = Command.from(stringArgs[0]);
        if (null == command) {
            throw new SystemRuntimeErrorException(turnContext, getCommandErrorMessage(turnContext));
        }
        return command;
    }

    public CommandLine getArgs(TurnContext turnContext, Command command, String[] stringArgs) {
        CommandLine args;
        try {
            args = commandLineParser.parse(command.getOptions(), stringArgs);
        } catch (ParseException e) {
            throw new SystemRuntimeErrorException(turnContext, getCommandErrorHelpMessage(e, command));
        }
        return args;
    }

    public String getCommandErrorMessage(TurnContext turnContext) {
        String errorMessageTemplate = "Not a command: \"{0}\", for help try \"help\"";
        return MessageFormat.format(errorMessageTemplate, turnContext.getActivity().getText());
    }

    public String getCommandErrorHelpMessage(ParseException exception, Command command) {
        String exceptionMessage = exception.getMessage() + "\n";
        String helpMenu = getHelpMenu(command);
        return exceptionMessage + helpMenu;
    }

    public String getAllCommandsHelp(boolean withArgs) {
        StringBuilder message = new StringBuilder();
        for (Command command : Command.values()) {
            if (command.isHidden()) {
                continue;
            }
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
        message.append("Available for ").append(getRolesText(command)).append("\n");
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

    private String getRolesText(Command command) {
        if (command.getRoles().isEmpty()) {
            return "everyone.";
        }
        StringBuilder sb = new StringBuilder();
        command.getRoles().forEach(role -> {
            sb.append(StringUtils.capitalize(role.getValue())).append(", ");
        });
        sb.setLength(sb.length() - 2);
        sb.append(".");
        return sb.toString();
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

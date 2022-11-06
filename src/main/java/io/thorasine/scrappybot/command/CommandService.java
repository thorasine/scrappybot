package io.thorasine.scrappybot.command;

import java.text.MessageFormat;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.command.commandline.CommandLineService;
import io.thorasine.scrappybot.command.deploy.DeployService;
import io.thorasine.scrappybot.command.help.HelpService;
import io.thorasine.scrappybot.command.kill.KillService;
import io.thorasine.scrappybot.command.release.ReleaseService;
import io.thorasine.scrappybot.command.restart.RestartService;
import io.thorasine.scrappybot.message.MessageService;
import io.thorasine.scrappybot.techcore.error.exception.SystemRuntimeErrorException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandService {

    private final HelpService helpService;
    private final KillService killService;
    private final DeployService deployService;
    private final ReleaseService releaseService;
    private final RestartService restartService;
    private final MessageService messageService;
    private final CommandLineService commandLineService;

    public void invokeFeature(TurnContext turnContext, String[] stringArgs) {
        CommandLine args;
        Command command = Command.from(stringArgs[0]);
        if (null == command) {
            throw new SystemRuntimeErrorException(turnContext, getCommandErrorMessage(turnContext));
        }
        try {
            args = commandLineService.parse(command, stringArgs);
        } catch (ParseException exception) {
            throw new SystemRuntimeErrorException(turnContext, commandLineService.getCommandErrorHelpMessage(exception, command));
        }
        invokeFeature(turnContext, command, args);
    }

    private void invokeFeature(TurnContext turnContext, Command command, CommandLine args) {
        switch (command) {
            case HELLO -> messageService.sendMessage(turnContext, "Hello back!");
            case HELP -> helpService.sendAllCommandsHelpMessage(turnContext, args);
            case DEPLOY -> deployService.deploy(turnContext, args);
            case RESTART -> restartService.restart(turnContext, args);
            case RELEASE -> releaseService.release(turnContext, args);
            case DELETE -> messageService.deleteMessage(turnContext);
            case KILL -> killService.kill(turnContext);
        }
    }

    private String getCommandErrorMessage(TurnContext turnContext) {
        String errorMessageTemplate = "Not a command: \"{0}\", for help try \"help\"";
        return MessageFormat.format(errorMessageTemplate, turnContext.getActivity().getText());
    }
}

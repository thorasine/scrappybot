package io.thorasine.scrappybot.command;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.command.deploy.DeployService;
import io.thorasine.scrappybot.command.help.HelpService;
import io.thorasine.scrappybot.command.kill.KillService;
import io.thorasine.scrappybot.command.release.ReleaseService;
import io.thorasine.scrappybot.command.restart.RestartService;
import io.thorasine.scrappybot.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
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

    public void hello(TurnContext turnContext) {
        messageService.sendMessage(turnContext, "Hello back!");
    }

    public void help(TurnContext turnContext, CommandLine args) {
        helpService.sendAllCommandsHelpMessage(turnContext, args);
    }

    public void deploy(TurnContext turnContext, CommandLine args) {
        deployService.deploy(turnContext, args);
    }

    public void release(TurnContext turnContext, CommandLine args) {
        releaseService.release(turnContext, args);
    }

    public void restart(TurnContext turnContext, CommandLine args) {
        restartService.restart(turnContext, args);
    }

    public void delete(TurnContext turnContext) {
        messageService.deleteMessage(turnContext);
    }

    public void kill(TurnContext turnContext) {
        killService.kill(turnContext);
    }
}

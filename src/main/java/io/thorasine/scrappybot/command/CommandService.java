package io.thorasine.scrappybot.command;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.command.deploy.DeployService;
import io.thorasine.scrappybot.command.help.HelpService;
import io.thorasine.scrappybot.command.kill.KillService;
import io.thorasine.scrappybot.command.release.ReleaseService;
import io.thorasine.scrappybot.command.restart.RestartService;
import io.thorasine.scrappybot.message.MessageService;
import io.thorasine.scrappybot.techcore.authorization.Authorize;
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

    @Authorize
    public void invokeFeature(TurnContext turnContext, Command command, CommandLine args) {
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
}

package io.thorasine.scrappybot.commands.help;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.commands.commandline.CommandLineService;
import io.thorasine.scrappybot.commands.commandline.enums.Command;
import io.thorasine.scrappybot.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelpService {

    private final MessageService messageService;
    private final CommandLineService commandLineService;

    public void getAllCommandsHelp(TurnContext turnContext, CommandLine args) {
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
        messageService.sendMessage(turnContext, message);
    }
}

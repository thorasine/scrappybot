package io.thorasine.scrappybot.commands.release;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReleaseService {

    private final MessageService messageService;

    public void release(TurnContext turnContext, CommandLine args) {
        String branch = args.getOptionValue("branch");
        String tag = args.getOptionValue("tag");
        if (args.hasOption("abort")) {
            //abort
        }
        messageService.sendMessage(turnContext,"WIP - Not implemented yet.");
    }
}

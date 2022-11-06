package io.thorasine.scrappybot.command.deploy;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.HeroCard;
import io.thorasine.scrappybot.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployService {

    private final MessageService messageService;
    private final DeployCardMakerUtil cardMakerService;
    private static final String TAG_PATTERN = "[0-9]{1,2}\\.[0-9]\\.[0-9]";

    public void deploy(TurnContext turnContext, CommandLine args) {
        String branch = null;
        if (ArrayUtils.isEmpty(args.getOptions())) {
            HeroCard deployCards = cardMakerService.createDeployCards();
            messageService.sendMessage(turnContext, MessageFactory.attachment(deployCards.toAttachment()));
            return;
        }
        if (args.hasOption("abort")) {
            messageService.sendMessage(turnContext, "Aborting deploy.");
            return;
        }
        if (args.hasOption("branch")) {
            branch = args.getOptionValue("branch");
        }
        if (args.hasOption("tag")) {
            branch = args.getOptionValue("tag");
        }
        String message = "Deploying " + branch + " to AWS.";
        messageService.sendMessage(turnContext, message);
    }
}

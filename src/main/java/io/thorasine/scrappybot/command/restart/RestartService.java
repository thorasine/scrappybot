package io.thorasine.scrappybot.command.restart;

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
public class RestartService {

    private final MessageService messageService;
    private final RestartCardMakerUtil cardMakerService;

    public void restart(TurnContext turnContext, CommandLine args) {
        if (ArrayUtils.isEmpty(args.getOptions())) {
            HeroCard deployCards = cardMakerService.createRestartCards();
            messageService.sendMessage(turnContext, MessageFactory.attachment(deployCards.toAttachment()));
            return;
        }
        String message = "Restarting the following service(s):";
        if (args.hasOption("lizi")) {
            message += " Lizi";
        }
        if (args.hasOption("lizi")) {
            message += " Video office";
        }
        if (args.hasOption("infra")) {
            message += " Infra";
        }
        if (args.hasOption("bot")) {
            message += " Bot";
        }
        messageService.sendMessage(turnContext, message);
    }
}

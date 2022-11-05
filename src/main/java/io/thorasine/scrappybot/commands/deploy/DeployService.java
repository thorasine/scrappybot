package io.thorasine.scrappybot.commands.deploy;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.ActionTypes;
import com.microsoft.bot.schema.CardAction;
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
    private static final String TAG_PATTERN = "[0-9]{1,2}\\.[0-9]\\.[0-9]";

    public void deploy(TurnContext turnContext, CommandLine args) {
        String branch = null;
        if (ArrayUtils.isEmpty(args.getOptions())) {
            HeroCard deployCards = createDeployCards();
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

    private HeroCard createDeployCards() {
        HeroCard card = new HeroCard();
        card.setTitle("Deploy");
        card.setSubtitle("Select branch to deploy");
        List<CardAction> buttons = new ArrayList<>();
        buttons.add(createDeployCard("develop"));
        buttons.add(createDeployCard("release/12.0.0"));
        buttons.add(createDeployCard("release/11.0.0"));

        CardAction abort = new CardAction();
        abort.setType(ActionTypes.MESSAGE_BACK);
        abort.setTitle("Abort");
        abort.setText("deploy -a");
        buttons.add(abort);

        CardAction delete = new CardAction();
        delete.setType(ActionTypes.MESSAGE_BACK);
        delete.setTitle("Exit");
        delete.setText("delete");
        buttons.add(delete);

        card.setButtons(buttons);
        return card;
    }

    private CardAction createDeployCard(String branch) {
        return createDeployCard(branch, branch);
    }

    private CardAction createDeployCard(String title, String branch) {
        CardAction cardAction = new CardAction();
        cardAction.setType(ActionTypes.MESSAGE_BACK);
        cardAction.setTitle(title);
        cardAction.setText("deploy -b " + branch);
        return cardAction;
    }
}

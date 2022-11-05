package io.thorasine.scrappybot.features.deploy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.ActionTypes;
import com.microsoft.bot.schema.CardAction;
import com.microsoft.bot.schema.HeroCard;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployService {

    private static final String TAG_PATTERN = "[0-9]{1,2}\\.[0-9]\\.[0-9]";

    public CompletableFuture<Void> deploy(TurnContext turnContext, CommandLine args) {
        String branch = null;
        if (ArrayUtils.isEmpty(args.getOptions())) {
            HeroCard deployCards = createDeployCards();
            return turnContext.sendActivity(MessageFactory.attachment(deployCards.toAttachment())).thenApply(sendResult -> null);
        }
        if (args.hasOption("abort")) {
            return turnContext.sendActivity(MessageFactory.text("Aborting deploy.")).thenApply(sendResult -> null);
        }
        if (args.hasOption("branch")) {
            branch = args.getOptionValue("branch");
        }
        if (args.hasOption("tag")) {
            branch = args.getOptionValue("tag");
        }
        String message = "Deploying " + branch + " to AWS.";
        return turnContext.sendActivity(MessageFactory.text(message)).thenApply(sendResult -> null);
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
        delete.setText("exit");
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

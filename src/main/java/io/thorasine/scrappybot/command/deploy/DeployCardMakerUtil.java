package io.thorasine.scrappybot.command.deploy;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.bot.schema.ActionTypes;
import com.microsoft.bot.schema.CardAction;
import com.microsoft.bot.schema.HeroCard;
import io.thorasine.scrappybot.techcore.properties.DeployProperties;
import io.thorasine.scrappybot.utility.CardMakerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployCardMakerUtil extends CardMakerUtil {

    private final DeployProperties deployProperties;

    public HeroCard createDeployCards() {
        HeroCard card = new HeroCard();
        card.setTitle("Deploy");
        card.setSubtitle("Select branch to deploy");
        List<CardAction> buttons = new ArrayList<>();
        deployProperties.getDefaultBranches().forEach(branch -> buttons.add(createDeployCard(branch)));
        buttons.add(createAbortCard());
        buttons.add(createDeleteCard());
        card.setButtons(buttons);
        return card;
    }

    public CardAction createDeployCard(String branch) {
        CardAction cardAction = new CardAction();
        cardAction.setType(ActionTypes.MESSAGE_BACK);
        cardAction.setTitle(branch);
        cardAction.setText("deploy -b " + branch);
        return cardAction;
    }

    public CardAction createAbortCard() {
        CardAction cardAction = new CardAction();
        cardAction.setType(ActionTypes.MESSAGE_BACK);
        cardAction.setTitle("Abort");
        cardAction.setText("deploy -a");
        return cardAction;
    }
}

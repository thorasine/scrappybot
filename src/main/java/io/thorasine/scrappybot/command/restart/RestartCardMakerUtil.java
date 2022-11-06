package io.thorasine.scrappybot.command.restart;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.bot.schema.ActionTypes;
import com.microsoft.bot.schema.CardAction;
import com.microsoft.bot.schema.HeroCard;
import io.thorasine.scrappybot.utility.CardMakerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static org.springframework.util.StringUtils.capitalize;

@Service
@RequiredArgsConstructor
public class RestartCardMakerUtil extends CardMakerUtil {

    public HeroCard createRestartCards() {
        HeroCard card = new HeroCard();
        card.setTitle("Restart");
        card.setSubtitle("Select service(s) to restart");
        List<CardAction> buttons = new ArrayList<>();
        buttons.add(createRestartCard("lizi"));
        buttons.add(createRestartCard("infra"));
        buttons.add(createRestartCard("bot"));
        buttons.add(createDeleteCard());
        card.setButtons(buttons);
        return card;
    }

    public CardAction createRestartCard(String service) {
        CardAction cardAction = new CardAction();
        cardAction.setType(ActionTypes.MESSAGE_BACK);
        cardAction.setTitle(capitalize(service));
        cardAction.setText("restart --" + service);
        return cardAction;
    }
}

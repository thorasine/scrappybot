package io.thorasine.scrappybot.utility;

import com.microsoft.bot.schema.ActionTypes;
import com.microsoft.bot.schema.CardAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardMakerUtil {

    public CardAction createDeleteCard() {
        CardAction cardAction = new CardAction();
        cardAction.setType(ActionTypes.MESSAGE_BACK);
        cardAction.setTitle("Exit");
        cardAction.setText("delete");
        return cardAction;
    }
}

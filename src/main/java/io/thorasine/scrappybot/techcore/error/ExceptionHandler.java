package io.thorasine.scrappybot.techcore.error;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExceptionHandler {

    private final MessageService messageService;

    public void handleException(SystemRuntimeErrorException e) {
        e.printStackTrace();
        messageService.sendMessage(e.getTurnContext(), e.getMessage());
    }

    public void handleException(Exception e, TurnContext turnContext) {
        e.printStackTrace();
        String message = "Error happened :(";
        if (e instanceof SystemRuntimeErrorException) {
            message = e.getMessage();
        }
        messageService.sendMessage(turnContext, message);
    }
}

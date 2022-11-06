package io.thorasine.scrappybot.techcore.error;

import com.microsoft.bot.builder.TurnContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemRuntimeErrorException extends RuntimeException {

    private final TurnContext turnContext;
    private final String message;

    public SystemRuntimeErrorException(TurnContext turnContext, String message) {
        super(message);
        this.turnContext = turnContext;
        this.message = message;
    }

}

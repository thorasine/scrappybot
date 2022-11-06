package io.thorasine.scrappybot.command.kill;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KillService {

    private final ApplicationContext appContext;
    private final MessageService messageService;

    public void kill(TurnContext turnContext) {
        messageService.sendMessage(turnContext,"Shutting down.");
        SpringApplication.exit(appContext, () -> 0);
    }
}

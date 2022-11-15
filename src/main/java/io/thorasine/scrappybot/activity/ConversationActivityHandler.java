package io.thorasine.scrappybot.activity;

import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.command.Command;
import io.thorasine.scrappybot.command.CommandService;
import io.thorasine.scrappybot.command.commandline.CommandLineService;
import io.thorasine.scrappybot.techcore.error.ExceptionHandler;
import io.thorasine.scrappybot.techcore.error.SystemRuntimeErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationActivityHandler extends ActivityHandler {

    private final CommandService commandService;
    private final CommandLineService commandLineService;
    private final ExceptionHandler exceptionHandler;

    @Override
    protected CompletableFuture<Void> onMessageActivity(TurnContext turnContext) {
        turnContext.getActivity().removeRecipientMention();
        log.info("From: {}, message: {}", turnContext.getActivity().getFrom().getName(), turnContext.getActivity().getText());
        try {
            processMessage(turnContext);
        } catch (Exception e) {
            exceptionHandler.handleException(e, turnContext);
        }
        return super.onMessageActivity(turnContext);
    }

    private void processMessage(TurnContext turnContext) throws Exception {
        String[] args = getArgs(turnContext.getActivity().getText());
        if (args.length == 0) {
            throw new SystemRuntimeErrorException(turnContext, "What?");
        }
        invokeFeature(turnContext, args);
    }

    private void invokeFeature(TurnContext turnContext, String[] stringArgs) throws Exception {
        Command command = commandLineService.getCommand(turnContext, stringArgs);
        CommandLine args = commandLineService.getArgs(turnContext, command, stringArgs);
        commandService.invokeFeature(turnContext, command, args);
    }

    private String[] getArgs(String text) {
        if (StringUtils.isNotBlank(text)) {
            return text.split(" ");
        }
        return new String[0];
    }
}

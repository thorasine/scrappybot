package io.thorasine.scrappybot.activity;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.ConversationReference;
import io.thorasine.scrappybot.command.CommandService;
import io.thorasine.scrappybot.command.commandline.CommandLineService;
import io.thorasine.scrappybot.command.commandline.enums.Command;
import io.thorasine.scrappybot.techcore.error.ExceptionHandler;
import io.thorasine.scrappybot.techcore.error.exception.SystemRuntimeErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationActivityHandler extends ActivityHandler {

    private final CommandService commandService;
    private final ExceptionHandler exceptionHandler;
    private final CommandLineService commandLineService;
    private final ConversationReferences conversationReferences;

    @Override
    protected CompletableFuture<Void> onMessageActivity(TurnContext turnContext) {
        addConversationReference(turnContext.getActivity());
        turnContext.getActivity().removeRecipientMention();
        log.info("From: {}, message: {}", turnContext.getActivity().getFrom().getName(), turnContext.getActivity().getText());
        try {
            processMessage(turnContext);
        } catch (Exception e) {
            exceptionHandler.handleException(e, turnContext);
        }
        return super.onMessageActivity(turnContext);
    }

    private void processMessage(TurnContext turnContext) {
        String[] args = getArgs(turnContext.getActivity().getText());
        if (args.length == 0) {
            throw new SystemRuntimeErrorException(turnContext, "What?");
        }
        invokeFeature(turnContext, args);
    }

    private void invokeFeature(TurnContext turnContext, String[] stringArgs) {
        CommandLine args;
        Command command = Command.from(stringArgs[0]);
        if (null == command) {
            throw new SystemRuntimeErrorException(turnContext, getCommandErrorMessage(turnContext));
        }
        try {
            args = commandLineService.parse(command, stringArgs);
        } catch (ParseException exception) {
            throw new SystemRuntimeErrorException(turnContext, commandLineService.getCommandErrorHelpMessage(exception, command));
        }
        switch (command) {
            case HELLO -> commandService.hello(turnContext);
            case HELP -> commandService.help(turnContext, args);
            case DEPLOY -> commandService.deploy(turnContext, args);
            case RESTART -> commandService.restart(turnContext, args);
            case RELEASE -> commandService.release(turnContext, args);
            case DELETE -> commandService.delete(turnContext);
            case KILL -> commandService.kill(turnContext);
        }
    }

    @Override
    protected CompletableFuture<Void> onConversationUpdateActivity(TurnContext turnContext) {
        addConversationReference(turnContext.getActivity());
        return super.onConversationUpdateActivity(turnContext);
    }

    private void addConversationReference(Activity activity) {
        ConversationReference conversationReference = activity.getConversationReference();
        conversationReferences.put(conversationReference.getUser().getId(), conversationReference);
    }

    private String getCommandErrorMessage(TurnContext turnContext) {
        String errorMessageTemplate = "Not a command: \"{0}\", for help try \"help\"";
        return MessageFormat.format(errorMessageTemplate, turnContext.getActivity().getText());
    }

    private String[] getArgs(String text) {
        if (StringUtils.isNotBlank(text)) {
            return text.split(" ");
        }
        return new String[0];
    }
}

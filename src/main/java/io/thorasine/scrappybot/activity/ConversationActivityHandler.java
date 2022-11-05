package io.thorasine.scrappybot.activity;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.Activity;
import com.microsoft.bot.schema.ConversationReference;
import io.thorasine.scrappybot.commands.commandline.CommandLineService;
import io.thorasine.scrappybot.commands.commandline.enums.Command;
import io.thorasine.scrappybot.commands.deploy.DeployService;
import io.thorasine.scrappybot.commands.help.HelpService;
import io.thorasine.scrappybot.commands.release.ReleaseService;
import io.thorasine.scrappybot.message.MessageService;
import io.thorasine.scrappybot.techcore.error.ExceptionHandler;
import io.thorasine.scrappybot.techcore.error.exception.SystemRuntimeErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationActivityHandler extends ActivityHandler {

    private final MessageService messageService;
    private final HelpService helpService;
    private final DeployService deployService;
    private final ReleaseService releaseService;
    private final ExceptionHandler exceptionHandler;
    private final CommandLineParser commandLineParser;
    private final CommandLineService commandLineService;
    private final ConversationReferences conversationReferences;

    @Override
    protected CompletableFuture<Void> onConversationUpdateActivity(TurnContext turnContext) {
        addConversationReference(turnContext.getActivity());
        return super.onConversationUpdateActivity(turnContext);
    }

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
        return new CompletableFuture<>();
    }

    private void processMessage(TurnContext turnContext) {
        String[] args = getArgs(turnContext.getActivity().getText());
        if (args.length == 0) {
            throw new SystemRuntimeErrorException(turnContext, "What?");
        }
        if (args[0].equalsIgnoreCase("exit")) {
            messageService.deleteMessage(turnContext);
            return;
        }
        invokeFeature(turnContext, args);
    }

    private void invokeFeature(TurnContext turnContext, String[] stringArgs) {
        Command command = Command.from(stringArgs[0]);
        if (null == command) {
            throw new SystemRuntimeErrorException(turnContext, getDefaultErrorMessage(turnContext));
        }
        CommandLine args;
        try {
            args = commandLineParser.parse(command.getOptions(), stringArgs);
        } catch (ParseException exception) {
            exception.printStackTrace();
            messageService.sendMessage(turnContext, commandLineService.getCommandErrorHelpMessage(turnContext, exception, command));
            return;
        }
        switch (command) {
            case HELP -> helpService.getAllCommandsHelp(turnContext, args);
            case RELEASE -> releaseService.release(turnContext, args);
            case DEPLOY -> deployService.deploy(turnContext, args);
        }
    }

    private void addConversationReference(Activity activity) {
        ConversationReference conversationReference = activity.getConversationReference();
        conversationReferences.put(conversationReference.getUser().getId(), conversationReference);
    }

    private String getDefaultErrorMessage(TurnContext turnContext) {
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

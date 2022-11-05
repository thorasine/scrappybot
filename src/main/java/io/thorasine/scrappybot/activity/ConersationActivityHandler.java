package io.thorasine.scrappybot.activity;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.ActivityHandler;
import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.ConversationReference;
import io.thorasine.scrappybot.features.common.enums.Command;
import io.thorasine.scrappybot.features.common.CommandLineService;
import io.thorasine.scrappybot.features.deploy.DeployService;
import io.thorasine.scrappybot.features.help.HelpService;
import io.thorasine.scrappybot.features.release.ReleaseService;
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
public class ConersationActivityHandler extends ActivityHandler {

    private final HelpService helpService;
    private final DeployService deployService;
    private final ReleaseService releaseService;
    private final CommandLineParser commandLineParser;
    private final CommandLineService commandLineService;
    private final ConversationReferences conversationReferences;

    @Override
    protected CompletableFuture<Void> onConversationUpdateActivity(TurnContext turnContext) {
        ConversationReference conversationReference = turnContext.getActivity().getConversationReference();
        conversationReferences.put(conversationReference.getUser().getId(), conversationReference);
        return super.onConversationUpdateActivity(turnContext);
    }

    @Override
    protected CompletableFuture<Void> onMessageActivity(TurnContext turnContext) {
        turnContext.getActivity().removeRecipientMention();
        log.info("From: {}, message: {}", turnContext.getActivity().getFrom().getName(), turnContext.getActivity().getText());
        String[] args = getArgs(turnContext.getActivity().getText());
        if (args.length == 0) {
            return turnContext.sendActivity(MessageFactory.text("What?")).thenApply(sendResult -> null);
        }
        if (args[0].equalsIgnoreCase("exit")) {
            return cancelCardActivity(turnContext);
        }
        return invokeFeature(turnContext, args);
    }

    private CompletableFuture<Void> invokeFeature(TurnContext turnContext, String[] stringArgs) {
        Command command = Command.from(stringArgs[0]);
        if (null == command) {
            return getDefaultErrorMessage(turnContext);
        }
        CommandLine args;
        try {
            args = commandLineParser.parse(command.getOptions(), stringArgs);
        } catch (ParseException exception) {
            exception.printStackTrace();
            return commandLineService.getCommandErrorHelpMessage(turnContext, exception, command);
        }
        return switch (command) {
            case HELP -> helpService.getAllCommandsHelp(turnContext, args);
            case RELEASE -> releaseService.release(turnContext, args);
            case DEPLOY -> deployService.deploy(turnContext, args);
        };
    }

    private CompletableFuture<Void> cancelCardActivity(TurnContext turnContext) {
        return turnContext.deleteActivity(turnContext.getActivity().getReplyToId());
    }

    private CompletableFuture<Void> getDefaultErrorMessage(TurnContext turnContext) {
        String errorMessageTemplate = "Not a command: \"{0}\", for help try \"help\"";
        String errorMessage = MessageFormat.format(errorMessageTemplate, turnContext.getActivity().getText());
        return turnContext.sendActivity(MessageFactory.text(errorMessage)).thenApply(sendResult -> null);
    }

    private String[] getArgs(String text) {
        if (StringUtils.isNotBlank(text)) {
            return text.split(" ");
        }
        return new String[0];
    }
}

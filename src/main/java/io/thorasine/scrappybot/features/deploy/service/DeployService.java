package io.thorasine.scrappybot.features.deploy.service;

import java.util.concurrent.CompletableFuture;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.features.common.enums.Command;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeployService {

    public CompletableFuture<Void> deploy(TurnContext turnContext, Command command, CommandLine args) {
        return null;
    }
}

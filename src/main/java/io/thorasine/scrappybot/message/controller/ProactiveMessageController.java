package io.thorasine.scrappybot.message.controller;

import io.thorasine.scrappybot.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProactiveMessageController {

    private final MessageService messageService;

    @GetMapping("/api/notify")
    public void sendMessageToEveryInteractiveUser() {
        messageService.sendGroupMessage("Proactive message");
    }
}

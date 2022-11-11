package io.thorasine.scrappybot.command.status;

import java.time.Duration;

import io.thorasine.scrappybot.command.status.dto.StatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class StatusRequestService {

    private static final Duration REQUEST_TIMEOUT = Duration.ofMillis(500);

    public StatusDTO getStatus(String url) {
        return WebClient.create(url)
            .get()
            .retrieve()
            .bodyToMono(StatusDTO.class)
            .block(REQUEST_TIMEOUT);
    }
}

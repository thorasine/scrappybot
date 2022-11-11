package io.thorasine.scrappybot.command.status;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.command.status.dto.StatusDTO;
import io.thorasine.scrappybot.message.MessageService;
import io.thorasine.scrappybot.techcore.properties.StatusProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final MessageService messageService;
    private final StatusProperties statusProperties;
    private final StatusRequestService statusRequestService;

    public void status(TurnContext turnContext, CommandLine args) {
        StringBuilder sb = new StringBuilder();
        for (StatusProperties.Service service : statusProperties.getServices()) {
            StatusDTO statusDTO = getStatusOfService(service);
            sb.append(statusDTO.getServiceName()).append(" - ").append(statusDTO.getStatus()).append("\n\n");
        }
        messageService.sendMessage(turnContext, sb.toString());
    }

    private StatusDTO getStatusOfService(StatusProperties.Service service) {
        StatusDTO status = buildWithDefaultStatus(service);
        try {
            status = statusRequestService.getStatus(service.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        status.setServiceName(service.getName());
        return status;
    }

    private StatusDTO buildWithDefaultStatus(StatusProperties.Service service) {
        return StatusDTO.builder()
            .serviceName(service.getName())
            .status("down")
            .build();
    }
}

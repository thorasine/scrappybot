package io.thorasine.scrappybot.techcore.permissions;

import java.util.List;
import java.util.Map;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.command.Command;
import io.thorasine.scrappybot.techcore.error.exception.SystemRuntimeErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final Map<String, Role> rolesByNames;

    public void hasPermission(TurnContext turnContext, Command command) {
        List<Role> requiredRoles = command.getRoles();
        if (requiredRoles.isEmpty()) {
            return;
        }
        String name = turnContext.getActivity().getFrom().getName();
        Role role = rolesByNames.get(turnContext.getActivity().getFrom().getName());
        if (null == role) {
            throw new SystemRuntimeErrorException(turnContext, "This operation requires a role but " + name + " doesn't have any.");
        }
        if (!command.getRoles().contains(role)) {
            throw new SystemRuntimeErrorException(turnContext, "Permission denied.");
        }

    }
}

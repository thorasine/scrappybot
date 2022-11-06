package io.thorasine.scrappybot.techcore.authorization;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.command.Command;
import io.thorasine.scrappybot.techcore.error.SystemRuntimeErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizeAspect {

    private final Map<String, Role> rolesByNames;

    @Around("@annotation(io.thorasine.scrappybot.techcore.authorization.Authorize)")
    public Object checkMaxFileSize(ProceedingJoinPoint joinPoint) throws Throwable {
        TurnContext turnContext = getTurnContext(joinPoint);
        Command command = getCommand(joinPoint);
        checkPermission(turnContext, command);
        return joinPoint.proceed();
    }

    public void checkPermission(TurnContext turnContext, Command command) {
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

    private TurnContext getTurnContext(ProceedingJoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs()).filter(o -> o instanceof TurnContext).map(o -> (TurnContext) o).findAny().get();
    }

    private Command getCommand(ProceedingJoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs()).filter(o -> o instanceof Command).map(o -> (Command) o).findAny().get();
    }
}

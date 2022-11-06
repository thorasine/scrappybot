package io.thorasine.scrappybot.techcore.authorization;

import java.util.HashMap;
import java.util.Map;

import io.thorasine.scrappybot.techcore.properties.RoleProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizeConfiguration {

    private final RoleProperties roleProperties;

    @Bean
    public Map<String, Role> rolesByNames() {
        Map<String, Role> rolesByNames = new HashMap<>();
        roleProperties.getBusiness().forEach(user -> {
            rolesByNames.put(user, Role.BUSINESS);
        });
        roleProperties.getDevelopers().forEach(user -> {
            rolesByNames.put(user, Role.DEVELOPER);
        });
        roleProperties.getQa().forEach(user -> {
            rolesByNames.put(user, Role.QA);
        });
        roleProperties.getOperators().forEach(user -> {
            rolesByNames.put(user, Role.OPERATOR);
        });
        return rolesByNames;
    }
}

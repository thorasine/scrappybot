package io.thorasine.scrappybot.techcore.configuration;

import java.util.HashMap;
import java.util.Map;

import io.thorasine.scrappybot.techcore.authorization.Role;
import io.thorasine.scrappybot.techcore.properties.RoleProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfigurationBeans {

    private final RoleProperties roleProperties;

    @Bean
    public CommandLineParser getCommandLineParser() {
        return new DefaultParser();
    }

    @Bean
    public HelpFormatter getHelpFormatter() {
        return new HelpFormatter();
    }

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

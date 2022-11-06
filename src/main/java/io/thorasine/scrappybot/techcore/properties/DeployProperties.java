package io.thorasine.scrappybot.techcore.properties;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bot.feature.deploy")
public class DeployProperties {

    private List<String> defaultBranches;

}
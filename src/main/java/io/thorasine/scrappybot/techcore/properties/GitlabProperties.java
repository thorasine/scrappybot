package io.thorasine.scrappybot.techcore.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bot.gitlab")
public class GitlabProperties {

    private String sitePath;
    private String accessToken;
    private String projectPath;

}
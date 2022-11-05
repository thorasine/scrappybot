package io.thorasine.scrappybot.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bot.microsoft")
public class MicrosoftAuthProperties {

    private String appId;

}
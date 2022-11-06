package io.thorasine.scrappybot.techcore.properties;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bot.roles")
public class RoleProperties {

    private List<String> operators;
    private List<String> developers;
    private List<String> qa;
    private List<String> business;

}
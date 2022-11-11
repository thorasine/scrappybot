package io.thorasine.scrappybot.techcore.properties;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "bot.feature.status")
public class StatusProperties {

    private List<Service> services;

    @Getter
    @Setter
    public static class Service {
        private String name;
        private String address;
    }
}

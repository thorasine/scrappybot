package io.thorasine.scrappybot.techcore.configuration;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationBeans {

    @Bean
    public CommandLineParser getCommandLineParser() {
        return new DefaultParser();
    }

    @Bean
    public HelpFormatter getHelpFormatter() {
        return new HelpFormatter();
    }
}

package io.thorasine.scrappybot.features.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.Options;

@Getter
@RequiredArgsConstructor
public enum Command {
    HELP("help", getHelpOptions()),
    RELEASE("release", getReleaseOptions()),
    DEPLOY("deploy", getDeployOptions());

    private final String value;
    private final Options options;

    public static Command from(String text) {
        for (Command command : Command.values()) {
            if (command.value.equalsIgnoreCase(text)) {
                return command;
            }
        }
        return null;
    }

    private static Options getReleaseOptions() {
        Options options = new Options();
        options.addRequiredOption("b", "branch", true, "Branch to create release from.");
        options.addRequiredOption("t", "tag", true, "Tag of the release.");
        return options;
    }

    private static Options getDeployOptions() {
        Options options = new Options();
        options.addOption("b", "branch", true, "Branch to deploy from.");
        options.addOption("t", "tag", true, "Tag to deploy from.");
        return options;
    }

    private static Options getHelpOptions() {
        return new Options();
    }
}

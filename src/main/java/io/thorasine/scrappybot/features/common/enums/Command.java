package io.thorasine.scrappybot.features.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.Options;

@Getter
@RequiredArgsConstructor
public enum Command {
    HELP("help", getHelpOptions(), "Show help menu."),
    RELEASE("release", getReleaseOptions(), "Release an app version through One Button Release job."),
    DEPLOY("deploy", getDeployOptions(), "Deploy the selected branch or tag on AWS.");

    private final String value;
    private final Options options;
    private final String description;

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
        options.addOption("a", "abort", false, "Abort current release process.");
        return options;
    }

    private static Options getDeployOptions() {
        Options options = new Options();
        options.addOption("b", "branch", true, "Branch to deploy from.");
        options.addOption("t", "tag", true, "Tag to deploy from.");
        options.addOption("a", "abort", false, "Abort current deploy.");
        return options;
    }

    private static Options getHelpOptions() {
        Options options = new Options();
        options.addOption("c", "command", true, "Show command args.");
        return options;
    }
}

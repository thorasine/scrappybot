package io.thorasine.scrappybot.commands.commandline.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.Options;

@Getter
@RequiredArgsConstructor
public enum Command {
    HELP("help", getHelpOptions(), false, "Show help menu."),
    HELLO("hello", getHelloOptions(), true, "Checks if the bot is alive."),
    DEPLOY("deploy", getDeployOptions(), false, "Deploy the selected branch or tag on AWS."),
    RESTART("restart", getRestartOptions(), false, "Restart the selected applications on AWS."),
    RELEASE("release", getReleaseOptions(), false, "Release an app version through One Button Release job."),
    DELETE("delete", getDeleteOptions(), true, "Deletes last bot message (for example deploy card on 'Exit'"),
    KILL("kill", getKillOptions(), true, "This kills the bot.");

    private final String value;
    private final Options options;
    private final boolean hidden;
    private final String description;

    public static Command from(String text) {
        for (Command command : Command.values()) {
            if (command.value.equalsIgnoreCase(text)) {
                return command;
            }
        }
        return null;
    }

    private static Options getHelpOptions() {
        Options options = new Options();
        options.addOption("c", "command", true, "Show command args.");
        return options;
    }

    private static Options getDeployOptions() {
        Options options = new Options();
        options.addOption("b", "branch", true, "Branch (or tag) to deploy from.");
        options.addOption("t", "tag", true, "Tag to deploy from.");
        options.addOption("a", "abort", false, "Abort current deploy.");
        return options;
    }

    private static Options getRestartOptions() {
        Options options = new Options();
        options.addOption("l", "lizi", false, "Restart lizi service.");
        options.addOption("i", "infra", false, "Restart the infra: db / clamav / maildev.");
        options.addOption("b", "bot", false, "Restart the bot.");
        return options;
    }

    private static Options getReleaseOptions() {
        Options options = new Options();
        options.addRequiredOption("b", "branch", true, "Branch to create release from.");
        options.addRequiredOption("t", "tag", true, "Tag of the release.");
        options.addOption("a", "abort", false, "Abort current release process.");
        return options;
    }

    private static Options getDeleteOptions() {
        return new Options();
    }

    private static Options getKillOptions() {
        return new Options();
    }

    private static Options getHelloOptions() {
        return new Options();
    }

}

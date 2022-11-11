package io.thorasine.scrappybot.command;

import java.util.List;

import io.thorasine.scrappybot.techcore.authorization.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.cli.Options;

import static io.thorasine.scrappybot.techcore.authorization.Role.BUSINESS;
import static io.thorasine.scrappybot.techcore.authorization.Role.DEVELOPER;
import static io.thorasine.scrappybot.techcore.authorization.Role.OPERATOR;
import static io.thorasine.scrappybot.techcore.authorization.Role.QA;

@Getter
@RequiredArgsConstructor
public enum Command {
    HELP("help", getHelpOptions(), getEmptyRoles(), false, "Show help menu."),
    HELLO("hello", getHelloOptions(), getEmptyRoles(), true, "Checks if the bot is alive."),
    STATUS("status", getStatusOptions(), getStatusRoles(), false, "Get current status of app services."),
    DEPLOY("deploy", getDeployOptions(), getDeployRoles(), false, "WIP - Deploy the selected branch or tag on AWS."),
    RESTART("restart", getRestartOptions(), getRestartRoles(), false, "WIP - Restart the selected applications on AWS."),
    RELEASE("release", getReleaseOptions(), gerReleaseRoles(), false, "Release an app version through One Button Release job."),
    DELETE("delete", getDeleteOptions(), getDeleteRoles(), true, "Deletes message it replies to (for example deploy card on 'Exit'"),
    KILL("kill", getKillOptions(), getKillRoles(), true, "This kills the bot.");

    private final String value;
    private final Options options;
    private final List<Role> roles;
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

    private static List<Role> getEmptyRoles() {
        return List.of();
    }

    private static List<Role> getStatusRoles() {
        return List.of();
    }

    private static List<Role> getDeployRoles() {
        return List.of(OPERATOR, DEVELOPER, QA, BUSINESS);
    }

    private static List<Role> getRestartRoles() {
        return List.of(OPERATOR);
    }

    private static List<Role> gerReleaseRoles() {
        return List.of(OPERATOR);
    }

    private static List<Role> getDeleteRoles() {
        return List.of(OPERATOR, DEVELOPER, QA, BUSINESS);
    }

    private static List<Role> getKillRoles() {
        return List.of(OPERATOR);
    }

    private static Options getHelpOptions() {
        Options options = new Options();
        options.addOption("c", "command", true, "Show command args.");
        return options;
    }

    private static Options getStatusOptions() {
        return new Options();
    }

    private static Options getHelloOptions() {
        return new Options();
    }

    private static Options getDeployOptions() {
        Options options = new Options();
        options.addOption("r", "reference", true, "Branch or tag to deploy from.");
        options.addOption("a", "abort", false, "Abort current deploy.");
        return options;
    }

    private static Options getRestartOptions() {
        Options options = new Options();
        options.addOption("l", "lizi", false, "Restart lizi service.");
        options.addOption("v", "video-office", false, "Restart video office service.");
        options.addOption("i", "infra", false, "Restart the infrastructure: db / clamav / maildev.");
        options.addOption("b", "bot", false, "Restart the bot.");
        return options;
    }

    private static Options getReleaseOptions() {
        Options options = new Options();
        options.addRequiredOption("r", "reference", true, "Reference branch or tag to create release from.");
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

}

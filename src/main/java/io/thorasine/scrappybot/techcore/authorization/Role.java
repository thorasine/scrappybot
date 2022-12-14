package io.thorasine.scrappybot.techcore.authorization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    OPERATOR("operator"),
    DEVELOPER("developer"),
    QA("QA"),
    BUSINESS("business");

    private final String value;
}

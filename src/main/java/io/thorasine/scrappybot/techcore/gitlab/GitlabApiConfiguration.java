package io.thorasine.scrappybot.techcore.gitlab;

import io.thorasine.scrappybot.techcore.properties.GitlabProperties;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GitlabApiConfiguration {

    private final GitlabProperties gitlabProperties;

    @Bean
    public GitLabApi gitLabApi() {
        return new GitLabApi(gitlabProperties.getSitePath(), gitlabProperties.getAccessToken());
    }
}

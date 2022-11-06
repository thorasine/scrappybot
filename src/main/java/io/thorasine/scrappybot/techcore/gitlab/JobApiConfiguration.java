package io.thorasine.scrappybot.techcore.gitlab;

import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.JobApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobApiConfiguration {

    private final GitLabApi gitLabApi;

    @Bean
    public JobApi jobApi() {
        return gitLabApi.getJobApi();
    }
}

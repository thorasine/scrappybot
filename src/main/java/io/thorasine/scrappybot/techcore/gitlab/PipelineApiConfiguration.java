package io.thorasine.scrappybot.techcore.gitlab;

import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.PipelineApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PipelineApiConfiguration {

    private final GitLabApi gitLabApi;

    @Bean
    public PipelineApi pipelineApi() {
        return gitLabApi.getPipelineApi();
    }
}

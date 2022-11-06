package io.thorasine.scrappybot.utility;

import io.thorasine.scrappybot.techcore.properties.GitlabProperties;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.models.Job;
import org.gitlab4j.api.models.Pipeline;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitlabUtil {

    private final GitlabProperties gitlabProperties;

    public String buildPipelineUrl(Pipeline pipeline) {
        return String.format("%s%s/-/pipelines/%s",
            gitlabProperties.getSitePath(),
            gitlabProperties.getProjectPath(),
            pipeline.getId());
    }

    public String buildArtifactDownloadUrl(Job job) {
        return String.format("%s%s/-/jobs/%s/artifacts/download",
            gitlabProperties.getSitePath(),
            gitlabProperties.getProjectPath(),
            job.getId());
    }
}

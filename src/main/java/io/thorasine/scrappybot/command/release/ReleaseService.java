package io.thorasine.scrappybot.command.release;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.microsoft.bot.builder.TurnContext;
import io.thorasine.scrappybot.message.MessageService;
import io.thorasine.scrappybot.techcore.error.SystemRuntimeErrorException;
import io.thorasine.scrappybot.techcore.properties.GitlabProperties;
import io.thorasine.scrappybot.utility.GitlabUtil;
import io.thorasine.scrappybot.utility.LinkUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.cli.CommandLine;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.JobApi;
import org.gitlab4j.api.PipelineApi;
import org.gitlab4j.api.models.Job;
import org.gitlab4j.api.models.Pipeline;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class ReleaseService {

    private final JobApi jobApi;
    private final LinkUtil linkUtil;
    private final GitlabUtil gitlabUtil;
    private final PipelineApi pipelineApi;
    private final MessageService messageService;
    private final GitlabProperties gitlabProperties;
    private final ReleaseAsyncService releaseAsyncService;

    public void release(TurnContext turnContext, CommandLine args) throws Exception {
        String reference = args.getOptionValue("reference");
        String tag = args.getOptionValue("tag");
        if (args.hasOption("abort")) {
            abort();
            messageService.sendMessage(turnContext, "Release aborted.");
            return;
        }
        if (releaseAsyncService.getRunningPipeline() != null) {
            throw new SystemRuntimeErrorException(turnContext, "A release is already on the way");
        }
        Pipeline pipeline = runPipeline(reference, tag);
        releaseAsyncService.setRunningPipeline(pipeline);
        releaseAsyncService.checkProgressAsync(turnContext);
        String message = "Release from " + reference + " with tag " + tag + " has begun.";
        String pipelineUrl = linkUtil.createLink("Link to pipeline", gitlabUtil.buildPipelineUrl(pipeline));
        messageService.sendMessage(turnContext, message + "\n" + pipelineUrl);
    }


    private Pipeline runPipeline(String reference, String tag) throws GitLabApiException {
        Map<String, String> variables = Map.of("TAG", tag);
        Pipeline pipeline = pipelineApi.createPipeline(gitlabProperties.getProjectPath(), reference, variables);
        List<Job> jobs = jobApi.getJobsForPipeline(gitlabProperties.getProjectPath(), pipeline.getId());
        stopBuildJobs(jobs);
        startReleaseFlowJobs(jobs);
        return pipeline;
    }

    private void stopBuildJobs(List<Job> jobs) throws GitLabApiException {
        for (Job job : jobs) {
            if (job.getStage().equalsIgnoreCase("build")) {
                jobApi.cancelJob(gitlabProperties.getProjectPath(), job.getId());
            }
        }
    }

    private List<Job> startReleaseFlowJobs(List<Job> jobs) throws GitLabApiException {
        List<Job> runningJobs = new ArrayList<>();
        for (Job job : jobs) {
            if (job.getStage().equalsIgnoreCase("release-flow")) {
                jobApi.playJob(gitlabProperties.getProjectPath(), job.getId());
                runningJobs.add(job);
            }
        }
        return runningJobs;
    }

    private void abort() throws GitLabApiException {
        pipelineApi.cancelPipelineJobs(gitlabProperties.getProjectPath(), releaseAsyncService.getRunningPipeline().getId());
        releaseAsyncService.clearState();
        //todo cleanup?
    }
}

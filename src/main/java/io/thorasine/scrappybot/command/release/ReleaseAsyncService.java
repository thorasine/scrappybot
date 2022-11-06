package io.thorasine.scrappybot.command.release;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.microsoft.bot.builder.MessageFactory;
import com.microsoft.bot.builder.TurnContext;
import com.microsoft.bot.schema.HeroCard;
import io.thorasine.scrappybot.message.MessageService;
import io.thorasine.scrappybot.techcore.properties.GitlabProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.JobApi;
import org.gitlab4j.api.models.Job;
import org.gitlab4j.api.models.JobStatus;
import org.gitlab4j.api.models.Pipeline;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static org.gitlab4j.api.models.JobStatus.CREATED;
import static org.gitlab4j.api.models.JobStatus.PENDING;
import static org.gitlab4j.api.models.JobStatus.RUNNING;
import static org.gitlab4j.api.models.JobStatus.SUCCESS;

@Slf4j
@Getter
@Setter
@Service
@RequiredArgsConstructor
public class ReleaseAsyncService {

    private final JobApi jobApi;
    private final MessageService messageService;
    private final GitlabProperties gitlabProperties;
    private final ReleaseCardMakerService releaseCardMakerService;
    private final Set<JobStatus> pendingJobStatuses = Set.of(CREATED, PENDING, RUNNING);
    private final Set<JobStatus> finishedJobStatuses = Set.of(SUCCESS);

    private Pipeline runningPipeline;

    @Async
    public void checkProgressAsync(TurnContext turnContext) throws Exception {
        log.info("Started release check progress async");
        int iteration = 0;
        boolean done = false;
        List<Job> jobs = new ArrayList<>();
        while (!done) {
            Thread.sleep(10000);
            if (iteration % 6 == 0) {
                log.info("Release check progress iteration: " + iteration);
            }
            iteration++;
            if (null == runningPipeline) {
                break;
            }
            jobs = getAllJobs();
            List<Job> pendingJobs = getRelevantJobs(jobs, pendingJobStatuses);
            if (pendingJobs.isEmpty()) {
                done = true;
            }
        }
        List<Job> finishedJobs = getRelevantJobs(jobs, finishedJobStatuses);
        sendArtifactDownloadMessage(turnContext, finishedJobs);
        clearState();
        log.info("Finished release check progress async");
    }

    public void clearState() {
        this.runningPipeline = null;
    }

    private List<Job> getAllJobs() throws GitLabApiException {
        return jobApi.getJobsForPipeline(gitlabProperties.getProjectPath(), runningPipeline.getId());
    }

    private List<Job> getRelevantJobs(List<Job> jobs, Set<JobStatus> statuses) {
        return jobs.stream()
            .filter(job -> job.getStage().equalsIgnoreCase("release-flow"))
            .filter(job -> statuses.contains(job.getStatus()))
            .toList();
    }

    private void sendArtifactDownloadMessage(TurnContext turnContext, List<Job> finishedJobs) {
        if (finishedJobs.isEmpty()) {
            return;
        }
        HeroCard artifactDownloadCard = releaseCardMakerService.createArtifactDownloadCard(finishedJobs);
        messageService.sendMessage(turnContext, MessageFactory.attachment(artifactDownloadCard.toAttachment()));
    }

}

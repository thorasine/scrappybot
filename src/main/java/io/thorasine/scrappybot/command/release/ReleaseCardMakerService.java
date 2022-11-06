package io.thorasine.scrappybot.command.release;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.bot.schema.ActionTypes;
import com.microsoft.bot.schema.CardAction;
import com.microsoft.bot.schema.HeroCard;
import io.thorasine.scrappybot.utility.GitlabUtil;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.models.Job;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReleaseCardMakerService {

    private final GitlabUtil gitlabUtil;

    public HeroCard createArtifactDownloadCard(List<Job> jobs) {
        HeroCard card = new HeroCard();
        List<CardAction> buttons = new ArrayList<>();
        card.setTitle("Release");
        card.setSubtitle("Release is finished, click to download the job artifacts.");
        jobs.forEach(job -> buttons.add(createArtifactDownloadButton(job)));
        card.setButtons(buttons);
        return card;
    }

    private CardAction createArtifactDownloadButton(Job job) {
        CardAction cardAction = new CardAction();
        cardAction.setType(ActionTypes.OPEN_URL);
        cardAction.setTitle(job.getName());
        cardAction.setValue(gitlabUtil.buildArtifactDownloadUrl(job));
        return cardAction;
    }
}

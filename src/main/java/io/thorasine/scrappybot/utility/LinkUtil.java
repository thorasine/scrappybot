package io.thorasine.scrappybot.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUtil {

    public String createLink(String text, String url) {
        return String.format("[%s](%s)", text, url);
    }
}

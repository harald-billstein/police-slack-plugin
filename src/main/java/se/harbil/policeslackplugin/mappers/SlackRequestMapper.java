package se.harbil.policeslackplugin.mappers;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import se.harbil.policeslackplugin.enums.SlackRequestParameters;
import se.harbil.policeslackplugin.model.RequestFromSlack;

@Slf4j
public class SlackRequestMapper {

    private static final String EMPTY_SPACE = " ";

    public static RequestFromSlack mapSlackBody(Map<String, String> slackBody) {
        return RequestFromSlack.builder()
            .token(slackBody.get(SlackRequestParameters.TOKEN.getName()))
            .teamId(slackBody.get(SlackRequestParameters.TEAM_ID.getName()))
            .teamDomain(slackBody.get(SlackRequestParameters.TEAM_DOMAIN.getName()))
            .channelId(slackBody.get(SlackRequestParameters.CHANNEL_ID.getName()))
            .channelName(slackBody.get(SlackRequestParameters.CHANNEL_NAME.getName()))
            .userId(slackBody.get(SlackRequestParameters.USER_ID.getName()))
            .userName(slackBody.get(SlackRequestParameters.USER_NAME.getName()))
            .command(slackBody.get(SlackRequestParameters.COMMAND.getName()))
            .text(slackBody.get(SlackRequestParameters.TEXT.getName()))
            .responseUrl(slackBody.get(SlackRequestParameters.RESPONSE_URL.getName()))
            .triggerId(slackBody.get(SlackRequestParameters.TRIGGER_ID.getName()))
            .county(extractCounty(slackBody.get(SlackRequestParameters.TEXT.getName())))
            .word(extractWord(slackBody.get(SlackRequestParameters.TEXT.getName())))
            .build();
    }

    private static String extractWord(String word) {
        if (hasExtraParameter(word)) {
            log.info("Found extra parameter(word): {}", word);
            int i = word.indexOf(EMPTY_SPACE);
            return word.substring(i + 1);
        } else {
            return null;
        }
    }

    private static String extractCounty(String county) {
        if (hasExtraParameter(county)) {
            log.info("Found extra parameter(word) while extracting county: {}", county);
            int i = county.indexOf(EMPTY_SPACE);
            return county.substring(0, i);
        } else {
            log.info("Extracting county: {}", county);
            return county;
        }
    }

    private static boolean hasExtraParameter(String county) {
        return county.contains(EMPTY_SPACE);
    }
}

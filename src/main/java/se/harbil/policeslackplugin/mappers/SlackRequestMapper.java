package se.harbil.policeslackplugin.mappers;

import lombok.extern.slf4j.Slf4j;
import se.harbil.policeslackplugin.enums.SlackRequestParameters;
import se.harbil.policeslackplugin.model.RequestFromSlack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
public class SlackRequestMapper {
    private static final String EMPTY_SPACE = " ";


    @ExceptionHandler({NullPointerException.class})
    public static RequestFromSlack mapSlackBody(Map<String, String> slackBody) {
        log.info("Mapping request: {}", slackBody);

        RequestFromSlack requestFromSlack = new RequestFromSlack();
        requestFromSlack.setToken(slackBody.get(SlackRequestParameters.TOKEN.getName()));
        requestFromSlack.setTeam_id(slackBody.get(SlackRequestParameters.TEAM_ID.getName()));
        requestFromSlack.setTeam_domain(slackBody.get(SlackRequestParameters.TEAM_DOMAIN.getName()));
        requestFromSlack.setChannel_id(slackBody.get(SlackRequestParameters.CHANNEL_ID.getName()));
        requestFromSlack.setChannel_name(slackBody.get(SlackRequestParameters.CHANNEL_NAME.getName()));
        requestFromSlack.setUser_id(slackBody.get(SlackRequestParameters.USER_ID.getName()));
        requestFromSlack.setUser_name(slackBody.get(SlackRequestParameters.USER_NAME.getName()));
        requestFromSlack.setCommand(slackBody.get(SlackRequestParameters.COMMAND.getName()));
        requestFromSlack.setText(slackBody.get(SlackRequestParameters.TEXT.getName()));
        requestFromSlack.setResponse_url(slackBody.get(SlackRequestParameters.RESPONSE_URL.getName()));
        requestFromSlack.setTrigger_id(slackBody.get(SlackRequestParameters.TRIGGER_ID.getName()));
        requestFromSlack.setCounty(extractCounty(slackBody.get(SlackRequestParameters.TEXT.getName())));
        requestFromSlack.setWord(extractWord(slackBody.get(SlackRequestParameters.TEXT.getName())));
        return requestFromSlack;
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

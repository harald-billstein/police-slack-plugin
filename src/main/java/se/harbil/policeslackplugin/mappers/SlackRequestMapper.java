package se.harbil.policeslackplugin.mappers;

import static se.harbil.policeslackplugin.enums.SlackRequestParameters.CHANNEL_ID;
import static se.harbil.policeslackplugin.enums.SlackRequestParameters.CHANNEL_NAME;
import static se.harbil.policeslackplugin.enums.SlackRequestParameters.COMMAND;
import static se.harbil.policeslackplugin.enums.SlackRequestParameters.RESPONSE_URL;
import static se.harbil.policeslackplugin.enums.SlackRequestParameters.TEAM_DOMAIN;
import static se.harbil.policeslackplugin.enums.SlackRequestParameters.TEAM_ID;
import static se.harbil.policeslackplugin.enums.SlackRequestParameters.TEXT;
import static se.harbil.policeslackplugin.enums.SlackRequestParameters.TOKEN;
import static se.harbil.policeslackplugin.enums.SlackRequestParameters.TRIGGER_ID;
import static se.harbil.policeslackplugin.enums.SlackRequestParameters.USER_ID;
import static se.harbil.policeslackplugin.enums.SlackRequestParameters.USER_NAME;

import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import se.harbil.policeslackplugin.model.RequestFromSlack;

@Slf4j
@UtilityClass
public class SlackRequestMapper {

    private static final String EMPTY_SPACE = " ";

    public RequestFromSlack mapSlackBody(Map<String, String> params) {
        return RequestFromSlack.builder()
            .token(params.get(TOKEN.getName()))
            .teamId(params.get(TEAM_ID.getName()))
            .teamDomain(params.get(TEAM_DOMAIN.getName()))
            .channelId(params.get(CHANNEL_ID.getName()))
            .channelName(params.get(CHANNEL_NAME.getName()))
            .userId(params.get(USER_ID.getName()))
            .userName(params.get(USER_NAME.getName()))
            .command(params.get(COMMAND.getName()))
            .text(params.get(TEXT.getName()))
            .responseUrl(params.get(RESPONSE_URL.getName()))
            .triggerId(params.get(TRIGGER_ID.getName()))
            .county(extractCountyParam(params.get(TEXT.getName())))
            .word(extractSearchParam(params.get(TEXT.getName())))
            .build();
    }

    private String extractSearchParam(String word) {
        if (hasExtraParameter(word)) {
            log.info("Found extra parameter(word): {}", word);
            int i = word.indexOf(EMPTY_SPACE);
            return word.substring(i + 1);
        } else {
            return null;
        }
    }

    private String extractCountyParam(String county) {
        if (hasExtraParameter(county)) {
            log.info("Found extra parameter(word) while extracting county: {}", county);
            int i = county.indexOf(EMPTY_SPACE);
            return county.substring(0, i);
        } else {
            log.info("Extracting county: {}", county);
            return county;
        }
    }

    private boolean hasExtraParameter(String county) {
        return county.contains(EMPTY_SPACE);
    }
}

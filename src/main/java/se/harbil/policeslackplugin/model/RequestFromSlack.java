package se.harbil.policeslackplugin.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestFromSlack {

    private String token;
    private String teamId;
    private String teamDomain;
    private String channelId;
    private String channelName;
    private String userId;
    private String userName;
    private String command;
    private String text;
    private String county;
    private String word;
    private String responseUrl;
    private String triggerId;
}

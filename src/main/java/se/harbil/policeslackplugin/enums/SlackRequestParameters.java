package se.harbil.policeslackplugin.enums;

public enum SlackRequestParameters {
    TOKEN("token"),
    TEAM_ID("team_id"),
    TEAM_DOMAIN("team_domain"),
    CHANNEL_ID("channel_id"),
    CHANNEL_NAME("channel_name"),
    USER_ID("user_id"),
    USER_NAME("user_name"),
    COMMAND("command"),
    TEXT("text"),
    RESPONSE_URL("response_url"),
    TRIGGER_ID("trigger_id");

    private String name;

     SlackRequestParameters(String name){
        this.name = name;
    }

    public String getName(){
         return name;
    }


}

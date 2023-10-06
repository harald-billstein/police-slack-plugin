package se.harbil.policeslackplugin.serivce;

import se.harbil.policeslackplugin.model.ResponseToSlack;

public interface SlackAnswerService {
    void sendReply(String response_url, ResponseToSlack requestFromSlack);
}

package se.harbil.policeslackplugin.controller;

import se.harbil.policeslackplugin.model.RequestFromSlack;

public interface SlackController {

    void startAsyncAnswer(RequestFromSlack request);
}

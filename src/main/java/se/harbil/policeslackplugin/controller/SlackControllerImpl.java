package se.harbil.policeslackplugin.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import se.harbil.policeslackplugin.enums.CountyEnums;
import se.harbil.policeslackplugin.model.PoliceRssResponseModel;
import se.harbil.policeslackplugin.model.RequestFromSlack;
import se.harbil.policeslackplugin.model.ResponseAttachment;
import se.harbil.policeslackplugin.model.ResponseToSlack;
import se.harbil.policeslackplugin.serivce.CrimeService;
import se.harbil.policeslackplugin.serivce.SlackAnswerService;

@Slf4j
@Controller
@AllArgsConstructor
public class SlackControllerImpl implements SlackController {

    private final CrimeService crimeService;
    private final SlackAnswerService slackAnswerService;

    public void startAsyncAnswer(RequestFromSlack request) {

        new Thread(() -> {
            log.info("Creating async response to user: {} url: {} county: {} word: {}",
                request.getUserName(),
                request.getResponseUrl(),
                request.getCounty(),
                request.getWord());

            createAsyncAnswer(request);
        }).start();

    }

    private void createAsyncAnswer(RequestFromSlack requestFromSlack) {
        ResponseToSlack responseToSlack;

        if (isInvalidOrHelpCommand(requestFromSlack)) {
            responseToSlack = loadHelpCommandsToUser();
        } else {
            CountyEnums countyEnums = getCountyEnum(requestFromSlack);
            ArrayList<String> list = new ArrayList<>();
            list.add(countyEnums.getValue);
            List<PoliceRssResponseModel> topics = crimeService.getTopics(list);

            // TODO sort out what user is interested in
            List<PoliceRssResponseModel> subscriberTopics = getSubscriberTopics(topics,
                requestFromSlack.getWord());

            responseToSlack = loadResponseToSlackWithData(subscriberTopics, requestFromSlack);

        }

        slackAnswerService.sendReply(requestFromSlack.getResponseUrl(), responseToSlack);
    }

    private boolean isInvalidOrHelpCommand(RequestFromSlack requestFromSlack) {
        return requestFromSlack.getCounty().equalsIgnoreCase("-help") || !isValidInput(
            requestFromSlack.getCounty().toUpperCase());
    }

    private boolean isValidInput(String county) {
        try {
            CountyEnums.valueOf(county);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private ResponseToSlack loadHelpCommandsToUser() {
        ResponseToSlack responseToSlack = new ResponseToSlack();
        responseToSlack.setResponse_type("ephemeral");
        responseToSlack.setText("Available commands: ");
        List<ResponseAttachment> responseAttachments = new ArrayList<>();
        ResponseAttachment responseAttachment = new ResponseAttachment();

        StringBuilder text = new StringBuilder(" || ");
        for (CountyEnums countyEnums : CountyEnums.values()) {
            text.append(countyEnums.name().toLowerCase()).append(" || ");


        }
        byte[] bytes = text.toString().getBytes();
        responseAttachment.setText(new String(bytes, StandardCharsets.ISO_8859_1));

        responseAttachments.add(responseAttachment);
        responseToSlack.setAttachments(responseAttachments);
        return responseToSlack;


    }

    private ResponseToSlack loadResponseToSlackWithData(List<PoliceRssResponseModel> topics,
        RequestFromSlack requestFromSlack) {
        ResponseToSlack responseToSlack = new ResponseToSlack();
        responseToSlack.setResponse_type("ephemeral");
        responseToSlack.setText("Reports found: ");

        List<ResponseAttachment> responseAttachments = new ArrayList<>();

        for (int i = 0; i < topics.size(); i++) {
            if (i > 4) {
                break;
            }

            ResponseAttachment responseAttachment = new ResponseAttachment();
            String text =
                topics.get(i).getTitle() + "\r\n" + topics.get(i).getDescription()
                    .substring(0, 1)
                    .toUpperCase()
                    + topics.get(i)
                    .getDescription()
                    .substring(1) + "\r\n" + topics.get(i)
                    .getLink();

            byte[] bytes = text.getBytes();

            responseAttachment.setText(new String(bytes, StandardCharsets.ISO_8859_1));
            responseAttachments.add(responseAttachment);

        }

        if (responseAttachments.isEmpty()) {
            ResponseAttachment responseAttachment = new ResponseAttachment();
            responseAttachment.setText("Nothing to Report");
            responseAttachments.add(responseAttachment);
        }

        responseToSlack.setAttachments(responseAttachments);

        return responseToSlack;
    }

    private CountyEnums getCountyEnum(RequestFromSlack requestFromSlack) {
        return CountyEnums.valueOf(requestFromSlack.getCounty().toUpperCase());
    }

    private List<PoliceRssResponseModel> getSubscriberTopics(
        List<PoliceRssResponseModel> topicsFromCounties, String word) {
        List<PoliceRssResponseModel> sortedTopics = new ArrayList<>();

        for (PoliceRssResponseModel topic : topicsFromCounties) {
            topic.setDescription(topic.getDescription().toLowerCase());
            if (word != null && topic.getDescription().contains(word.toLowerCase())) {
                sortedTopics.add(topic);
            } else if (word == null) {
                sortedTopics.add(topic);
            }
        }

        return sortedTopics;
    }
}

package se.harbil.policeslackplugin.api.endpoint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.harbil.policeslackplugin.controller.SlackControllerImpl;
import se.harbil.policeslackplugin.mappers.SlackRequestMapper;
import se.harbil.policeslackplugin.model.RequestFromSlack;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class PoliceSlackPluginApiEndPoint {

    private final SlackControllerImpl slackControllerImpl;
    @Value("${slack.client.secret}")
    private String slackClientSecret;
    @Value("${slack.client.id}")
    private String slackClientId;
    @Value("${slack.redirect.url}")
    private String slackRedirectUrl;


    @PostMapping("/report")
    public void getReport(@RequestParam Map<String, String> slackBody) {
        RequestFromSlack requestFromSlack = SlackRequestMapper.mapSlackBody(slackBody);
        slackControllerImpl.startAsyncAnswer(requestFromSlack);
    }

    @PostMapping("/authorize")
    public String getAuthorize(@RequestHeader Map<String, String> headers, @RequestParam String code,
        @RequestParam String state) {
        log.info("Authorize header: {} code: {} code: {}", headers, code, state);

        test(code);
        return "index.html";
    }

    private void test(String code) {
        try {
            URL url = new URL("https://slack.com/api/oauth.v2.access?code=" + code
                + "&client_id=" + slackClientId + "&client_secret=" + slackClientSecret + "&redirect_uri=" + slackRedirectUrl);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            con.getResponseCode();

            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            log.info("Content: {}", content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

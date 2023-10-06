package se.harbil.policeslackplugin.endpoint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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


    @RequestMapping("/report")
    public void getReport(@RequestParam Map<String, String> slackBody) {
        RequestFromSlack requestFromSlack = SlackRequestMapper.mapSlackBody(slackBody);
        slackControllerImpl.startAsyncAnswer(requestFromSlack);
    }

    @RequestMapping("/authorize")
    public String getAuthorize(@RequestHeader Map<String, String> headers, @RequestParam String code,
        @RequestParam String state) {
        log.info("Authorize header: {} code: {} code: {}", headers, code, state);

        test(code);
        return "index.html";
    }

    private void test(String code) {
        // curl -F code=1234 -F client_id=3336676.569200954261 -F client_secret=ABCDEFGH https:
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

            // Map<String, String> parameters = new HashMap<>();
            // parameters.put("code", code);

            //con.setDoOutput(true);
            //DataOutputStream out = new DataOutputStream(con.getOutputStream());
            //out.writeBytes(getParamsString(parameters));
            //out.flush();
            //out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getParamsString(Map<String, String> params)
        throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
            ? resultString.substring(0, resultString.length() - 1)
            : resultString;
    }

}

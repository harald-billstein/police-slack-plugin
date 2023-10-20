package se.harbil.policeslackplugin.serivce;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.harbil.policeslackplugin.model.ResponseToSlack;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackAnswerServiceImpl implements SlackAnswerService {


    private final ObjectMapper objectMapper;

    @Override
    public void sendReply(String response_url, ResponseToSlack responseToSlack) {
        log.info("Trying to Answer back to slack");
        try {
            URL url = new URL(response_url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestProperty("Content-Type", "application/json");

            DataOutputStream outputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
            outputStream.writeBytes(objectMapper.writeValueAsString(responseToSlack));
            outputStream.close();

            int responseCode = httpsURLConnection.getResponseCode();

            if (responseCode == 200) {
                log.info("Successfully Answered back to slack with response: {}",
                    objectMapper.writeValueAsString(responseToSlack));
            } else {
                throw new IOException("Bad response code: " + responseCode);
            }
        } catch (IOException e) {
            log.error("Failed to answer back to slack error: {}", e.getMessage());
        }
    }
}

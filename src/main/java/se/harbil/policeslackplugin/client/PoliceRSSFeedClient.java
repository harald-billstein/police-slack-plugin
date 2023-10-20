package se.harbil.policeslackplugin.client;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@AllArgsConstructor
public class PoliceRSSFeedClient {

    @Qualifier("policeRSSFeedClient")
    private final WebClient policeRSSFeedClient;

    public String call(String localFeed) {
        return policeRSSFeedClient.get()
            .uri(localFeed)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

}

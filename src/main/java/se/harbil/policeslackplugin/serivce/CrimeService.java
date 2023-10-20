package se.harbil.policeslackplugin.serivce;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.harbil.policeslackplugin.client.PoliceRSSFeedClient;
import se.harbil.policeslackplugin.model.PoliceRssResponseModel;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrimeService {

    private final PoliceRSSFeedClient policeRSSFeedClient;
    private final ObjectMapper objectMapper;
    private final XmlMapper xmlMapper = new XmlMapper();

    public List<PoliceRssResponseModel> getTopics(List<String> localFeeds) {
        List<PoliceRssResponseModel> policeRssResponseModelList = new ArrayList<>();
        for (String localFeed : localFeeds) {
            try {
                log.info("Trying to retrieve incidents from polisen.se");

                String response = policeRSSFeedClient.call(localFeed);
                policeRssResponseModelList.addAll(parseXML2(response));

                log.info("Successfully retrieved incidents from polisen.se");
            } catch (Exception e) {
                log.warn("Failed to retrieve or parse from polisen.se");
            }
        }
        return policeRssResponseModelList;
    }

    private List<PoliceRssResponseModel> parseXML2(String XMLString) throws Exception {
        JsonNode jsonNode = xmlMapper.readTree(XMLString);
        String string = jsonNode.get("channel")
            .get("item")
            .toString();
        return objectMapper.readValue(string, new TypeReference<>() {
        });
    }

}

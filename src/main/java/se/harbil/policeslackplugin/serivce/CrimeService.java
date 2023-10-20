package se.harbil.policeslackplugin.serivce;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import se.harbil.policeslackplugin.client.PoliceRSSFeedClient;
import se.harbil.policeslackplugin.model.PoliceRssResponseModel;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrimeService {

    private final PoliceRSSFeedClient policeRSSFeedClient;

    public List<PoliceRssResponseModel> getTopics(ArrayList<String> localFeeds) {
        List<PoliceRssResponseModel> policeRssResponseModelList = new ArrayList<>();
        for (String localFeed : localFeeds) {
            try {
                log.info("Trying to retrieve incidents from polisen.se");

                String response = policeRSSFeedClient.call(localFeed);
                policeRssResponseModelList.addAll(parseXML(response));

                log.info("Successfully retrieved incidents from polisen.se");
            } catch (Exception e) {
                log.warn("Failed to retrieve or parse from polisen.se");
            }
        }
        return policeRssResponseModelList;
    }


    private List<PoliceRssResponseModel> parseXML(String XMLString) throws Exception {
        List<PoliceRssResponseModel> policeRssResponseModelList = new ArrayList<>();
        Document doc;

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource src = new InputSource();
        src.setCharacterStream(new StringReader(XMLString));
        doc = builder.parse(src);

        for (int i = 0; i < doc.getElementsByTagName("item").getLength(); i++) {
            policeRssResponseModelList.add(PoliceRssResponseModel.builder()
                .title(doc.getElementsByTagName("title").item(i + 1).getTextContent())
                .link(doc.getElementsByTagName("link").item(i + 1).getTextContent())
                .guid(doc.getElementsByTagName("guid").item(i).getTextContent())
                .description(doc.getElementsByTagName("description").item(i + 1).getTextContent())
                .guid(doc.getElementsByTagName("guid").item(i).getTextContent())
                .pubDate(doc.getElementsByTagName("pubDate").item(i).getTextContent()).build());
        }

        return policeRssResponseModelList;
    }
}

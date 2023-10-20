package se.harbil.policeslackplugin.serivce;

import static java.net.HttpURLConnection.HTTP_OK;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import reactor.netty.http.client.HttpClient;
import se.harbil.policeslackplugin.model.PoliceRssResponseModel;

@Slf4j
@Service
public class CrimeService {

    private final static String baseURL = "https://polisen.se/aktuellt/rss/";

    public List<PoliceRssResponseModel> getTopics(ArrayList<String> localFeeds) {
        List<PoliceRssResponseModel> policeRssResponseModelList = new ArrayList<>();
        for (String localFeed : localFeeds) {
            try {
                log.info("Trying to retrieve incidents from polisen.se");
                String response = call2(localFeed);
                log.info("Successfully retrieved incidents from polisen.se");
                log.info("Trying to parse incidents from polisen.se");
                policeRssResponseModelList.addAll(parseXML(response));
                log.info("Successfully parsed incidents from polisen.se");
            } catch (Exception e) {
                log.warn("Failed to retrieve or parse from polisen.se");
            }
        }
        return policeRssResponseModelList;
    }

    private String call(String localFeed) throws IOException {

        String response;
        URL url;

        url = new URL(baseURL + localFeed);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        if (con.getResponseCode() == HTTP_OK) {
            response = extractContent(con);
        } else {
            response = con.getResponseMessage();
        }

        return response;
    }

    @SneakyThrows
    public String call2(String localFeed) {
        WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(getInsecureHttpClient()))
            .baseUrl(baseURL + localFeed)
            .build();

        return webClient.get()
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    private HttpClient getInsecureHttpClient() throws SSLException {
        SslContext sslContext = SslContextBuilder
            .forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build();
        return HttpClient.create().secure(t -> t.sslContext(sslContext));
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

    private String extractContent(HttpsURLConnection con) throws IOException {
        StringBuilder output = new StringBuilder();
        if (con != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String input;
            while ((input = br.readLine()) != null) {
                output.append(input);
            }
            br.close();
        }
        return output.toString();
    }
}

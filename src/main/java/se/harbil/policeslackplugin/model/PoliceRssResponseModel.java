package se.harbil.policeslackplugin.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class PoliceRssResponseModel {

    private String guid;
    private String title;
    @Setter
    private String description;
    private String pubDate;
    private String link;

}

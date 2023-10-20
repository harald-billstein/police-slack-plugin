package se.harbil.policeslackplugin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PoliceRssResponseModel {

    private String title;
    private String description;
    private String pubDate;
    private String link;

}

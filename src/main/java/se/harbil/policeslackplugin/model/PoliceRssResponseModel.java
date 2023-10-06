package se.harbil.policeslackplugin.model;

public class PoliceRssResponseModel {

    private String guid;
    private String title;
    private String description;
    private String pubDate;
    private String link;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
            "guid='" + guid + '\'' +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", pubDate='" + pubDate + '\'' +
            ", link='" + link + '\'' +
            '}';
    }
}

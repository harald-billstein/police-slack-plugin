package se.harbil.policeslackplugin.enums;

public enum CountyEnums {
    BLEKINGE("blekinge/handelser-rss---blekinge/"),
    DALARNA("dalarna/handelser-rss---dalarna/"),
    GOTLAND("gotland/handelser-rss---gotland/"),
    GÄVLEBORG("gavleborg/handelser-rss---gavleborg/"),
    HALLAND("halland/handelser-rss---halland/"),
    JÄMTLAND("jamtland/handelser-rss---jamtland/"),
    JÖNKÖPING("jonkopings-lan/handelser-rss---jonkoping/"),
    KALMARLÄN("kalmar-lan/handelser-rss---kalmar-lan/"),
    KRONOBERG("kronoberg/handelser-rss---kronoberg/"),
    NORRBOTTEN("norrbotten/handelser-rss---norrbotten/"),
    SKÅNE("skane/handelser-rss---skane/"),
    STOCKHOLMSLÄN("stockholms-lan/handelser-rss---stockholms-lan/"),
    SÖDERMANLAND("sodermanland/handelser-rss---sodermanland/"),
    UPPSALALÄN("uppsala-lan/handelser-rss---uppsala-lan/"),
    VÄRMLAND("varmland/handelser-rss---varmland/"),
    VÄSTERBOTTEN("vasterbotten/handelser-rss---vasterbotten/"),
    VÄSTERNORRLAND("vasternorrland/handelser-rss---vasternorrland/"),
    VÄSTMANLAND("vastmanland/handelser-rss---vastmanland/"),
    VÄSTRAGÖTALAND("vastra-gotaland/handelser-rss---vastra-gotaland/"),
    ÖREBROLÄN("orebro-lan/handelser-rss---orebro-lan/"),
    ÖSTERGÖTLAND("ostergotland/handelser-rss---ostergotland/");

    public final String getValue;

    CountyEnums(String county) {
        this.getValue = county;
    }
}

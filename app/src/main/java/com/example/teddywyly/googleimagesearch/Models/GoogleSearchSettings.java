package com.example.teddywyly.googleimagesearch.Models;

import java.io.Serializable;

/**
 * Created by teddywyly on 5/13/15.
 */
public class GoogleSearchSettings implements Serializable {


    public enum ImageSize {
        SMALL("icon"), MEDIUM("small|medium|large|xlarge"), LARGE("xxlarge"), EXTRA_LARGE("huge");
        private String value;
        private ImageSize(String value) {
            this.value = value;
        }
    }

    public enum ImageColor {
        BLACK("black"), BLUE("blue"), BROWN("brown"), GRAY("gray"), GREEN("green"),
        ORANGE("orange"), PINK("pink"), PURPLE("purple"), RED("red"), TEAL("teal"),
        WHITE("white"), YELLOW("yellow");
        private String value;
        private ImageColor(String value) {
            this.value = value;
        }
    }

    public enum ImageType {
        FACE("face"), PHOTO("photo"), CLIPART("clipart"), LINEART("lineart");
        private String value;
        private ImageType(String value) {
            this.value = value;
        }
    }

    public enum ImageRights {
        PUBLIC_DOMAIN("cc_publicdomain"), ATTRIBUTE("cc_attribute"), NON_COMMERCIAL("cc_noncommercial");
        private String value;
        private ImageRights(String value) {
            this.value = value;
        }
    }

    private static final long serialVersionUID = 5249605308630544032L;
    public ImageSize size;
    public ImageColor color;
    public ImageType type;
    public ImageRights rights;
    public String siteFilter;
    public int resultsPerPage;

    public GoogleSearchSettings(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public String settingsAsQueryString() {
        String url = "&rsz=" + resultsPerPage;
        if (size != null) {
            url = url + "&imgsz=" + size.value;
        }
        if (color != null) {
            url = url + "&imgcolor=" + color.value;
        }
        if (type != null) {
            url = url + "&imgtype=" + type.value;
        }
        if (rights != null) {
            url = url + "&as_rights=" + rights.value;
        }
        if (siteFilter != null) {
            url = url + "&as_sitesearch=" + siteFilter;
        }
        return url;
    }
}

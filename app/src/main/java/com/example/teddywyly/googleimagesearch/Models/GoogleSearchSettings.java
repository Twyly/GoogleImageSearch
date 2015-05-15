package com.example.teddywyly.googleimagesearch.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by teddywyly on 5/13/15.
 */


// Having the displayname in here is a violation of MVC, but due to the excessive switch statements
    // it saves, it seems worth it

public class GoogleSearchSettings implements Serializable {


    public enum ImageSize {
        ANY(null, "Any"), SMALL("icon", "Small"), MEDIUM("small|medium|large|xlarge", "Medium"), LARGE("xxlarge", "Large"), EXTRA_LARGE("huge", "Extra Large");
        private String value;
        private String displayName;
        private ImageSize(String value, String displayName) {
            this.value = value;
            this.displayName = displayName;
        }
        @Override
        public String toString() {
            return displayName;
        }
    }

    public enum ImageColor {
        ANY(null, "Any"), BLACK("black", "Black"), BLUE("blue", "Blue"), BROWN("brown", "Brown"), GRAY("gray", "Gray"), GREEN("green", "Green"),
        ORANGE("orange", "Orange"), PINK("pink", "Pink"), PURPLE("purple", "Purple"), RED("red", "Red"), TEAL("teal", "Teal"),
        WHITE("white", "White"), YELLOW("yellow", "Yellow");
        private String value;
        private String displayName;
        private ImageColor(String value, String displayName) {
            this.value = value;
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public enum ImageType {
        ANY(null, "Any"), FACE("face", "Face"), PHOTO("photo", "Photo"), CLIPART("clipart", "Clipart"), LINEART("lineart", "Lineart");
        private String value;
        private String displayName;
        private ImageType(String value, String displayName) {
            this.value = value;
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public enum ImageRights {
        ANY(null, "Any"), PUBLIC_DOMAIN("cc_publicdomain", "Public Domain"), ATTRIBUTE("cc_attribute", "Attributable"), NON_COMMERCIAL("cc_noncommercial", "Noncommerical");
        private String value;
        private String displayName;
        private ImageRights(String value, String displayName) {
            this.value = value;
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private static final long serialVersionUID = 5249605308630544032L;
    public ImageSize size = ImageSize.ANY;
    public ImageColor color = ImageColor.ANY;
    public ImageType type = ImageType.ANY;
    public ImageRights rights = ImageRights.ANY;
    public String siteFilter;


    public HashMap<String, String>settingsAsParameters() {
        HashMap<String, String> params = new HashMap<String, String>();
        if (size != ImageSize.ANY) {
            params.put("imgsz", size.value);
        }
        if (color != ImageColor.ANY) {
            params.put("imgcolor", color.value);
        }
        if (type != ImageType.ANY) {
            params.put("imgtype", type.value);
        }
        if (rights != ImageRights.ANY) {
            params.put("as_rights", rights.value);
        }
        if (siteFilter != null) {
            params.put("as_sitesearch", siteFilter);
        }
        return params;
    }
}

package net.lortservers.iris.utils.webhooks.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Embed {
    private String title;
    private String description;
    private String url;
    @JsonIgnore
    private Date timestamp;
    private int color;
    private Footer footer;
    private Media image;
    private Media thumbnail;
    private Media video;
    private Author author;
    @Singular
    private List<Field> fields;

    @JsonProperty("timestamp")
    public String getTimestampJson() {
        return Instant.ofEpochMilli(timestamp.getTime()).toString();
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String date) {
        this.timestamp = Date.from(Instant.parse(date));
    }

    /**
     * <p>A data holder for the footer data type of the embed.</p>
     */
    @Getter
    @ToString
    @EqualsAndHashCode
    @Builder(toBuilder = true)
    public static class Footer {
        @Builder.Default
        private String text = "";
        @JsonProperty("icon_url")
        private String iconUrl;
    }

    /**
     * <p>A data holder for the author data type of the embed.</p>
     */
    @Getter
    @ToString
    @EqualsAndHashCode
    @Builder(toBuilder = true)
    public static class Author {
        private String name;
        private String url;
        @JsonProperty("icon_url")
        private String iconUrl;
    }

    /**
     * <p>A data holder for the media data type of the embed.</p>
     */
    @Getter
    @ToString
    @EqualsAndHashCode
    @Builder(toBuilder = true)
    public static class Media {
        private String url;
        private int height;
        private int width;
    }

    /**
     * <p>A data holder for the field data type of the embed.</p>
     */
    @Getter
    @ToString
    @EqualsAndHashCode
    @Builder(toBuilder = true)
    public static class Field {
        private String name;
        private String value;
        @Builder.Default
        private boolean inline = false;
    }
}

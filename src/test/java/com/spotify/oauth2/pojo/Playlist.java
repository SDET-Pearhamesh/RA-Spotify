
package com.spotify.oauth2.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter @Setter // Just add these two annotations to add setters and getters
@Builder // Creates builder pattern static method that return the Build class
@Jacksonized // To let lombok understand the annotations used Jackson dependency like below one

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Playlist {

    @JsonProperty("collaborative")
    private Boolean collaborative;

    @JsonProperty("description")
    private String description;

    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    @JsonProperty("followers")
    private Followers followers;

    @JsonProperty("href")
    private String href;

    @JsonProperty("id")
    private String id;

    @JsonProperty("images")
    private Object images;

    @JsonProperty("name")
    private String name;

    @JsonProperty("owner")
    private Owner owner;

    @JsonProperty("primary_color")
    private Object primaryColor;

    @JsonProperty("public")
    private Boolean _public;

    @JsonProperty("snapshot_id")
    private String snapshotId;

    @JsonProperty("tracks")
    private Tracks tracks;

    @JsonProperty("type")
    private String type;

    @JsonProperty("uri")
    private String uri;


}

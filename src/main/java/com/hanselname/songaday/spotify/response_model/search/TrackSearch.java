package com.hanselname.songaday.spotify.response_model.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class TrackSearch {
    private String id;
    private String name;
    private AlbumSearch album;
    private List<ArtistSearch> artists;
    @JsonProperty("external_urls")
    private ExternalUrls externalUrls;

    public static final class ExternalUrls {
        private String spotify;

        public String getSpotify() {
            return spotify;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AlbumSearch getAlbum() {
        return album;
    }

    public List<ArtistSearch> getArtists() {
        return artists;
    }

    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }
}

package com.hanselname.songaday.spotify.response_model.search;

import java.util.List;

public class TrackSearch {
    private String id;
    private String name;
    private AlbumSearch album;
    private List<ArtistSearch> artists;

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
}

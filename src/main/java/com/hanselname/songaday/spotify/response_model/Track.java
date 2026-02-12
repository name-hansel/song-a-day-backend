package com.hanselname.songaday.spotify.response_model;

import java.util.List;

public class Track {
    private String name;
    private Album album;
    private List<Artist> artists;

    public String getName() {
        return name;
    }

    public Album getAlbum() {
        return album;
    }

    public List<Artist> getArtists() {
        return artists;
    }
}

package com.hanselname.songaday.spotify.response_model.search;

import com.hanselname.songaday.spotify.response_model.Image;

import java.util.List;

public class AlbumSearch {
    private String name;
    private List<Image> images;

    public String getName() {
        return name;
    }

    public List<Image> getImages() {
        return images;
    }
}

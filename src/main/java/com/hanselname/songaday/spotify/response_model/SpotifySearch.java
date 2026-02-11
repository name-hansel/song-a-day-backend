package com.hanselname.songaday.spotify.response_model;

import java.util.List;

public class SpotifySearch {
    private TrackContainer tracks;

    public TrackContainer getTracks() {
        return tracks;
    }

    public static class TrackContainer {
        private List<Track> items;

        public List<Track> getItems() {
            return items;
        }
    }
}

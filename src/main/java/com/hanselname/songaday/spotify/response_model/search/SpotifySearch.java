package com.hanselname.songaday.spotify.response_model.search;

import java.util.List;

public final class SpotifySearch {
    private TrackContainer tracks;

    public TrackContainer getTracks() {
        return tracks;
    }

    public static final class TrackContainer {
        private List<TrackSearch> items;

        public List<TrackSearch> getItems() {
            return items;
        }
    }
}

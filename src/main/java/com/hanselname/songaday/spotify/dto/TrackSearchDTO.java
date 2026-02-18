package com.hanselname.songaday.spotify.dto;

import java.io.Serializable;

public record TrackSearchDTO(String spotifyId, String trackName, String artistName, String albumName,
                             String imageUrl) implements Serializable {
}

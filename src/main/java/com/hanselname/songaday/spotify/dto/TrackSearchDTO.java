package com.hanselname.songaday.spotify.dto;

import java.io.Serializable;
import java.util.Set;

public record TrackSearchDTO(String spotifyTrackId, String trackName, Set<String> spotifyArtistIds, String artistName,
                             String spotifyAlbumId, String albumName, String spotifyUrl, String largeImageUrl,
                             String mediumImageUrl, String smallImageUrl) implements Serializable {
}

package com.hanselname.songaday.song.entity;

import com.hanselname.songaday.common.entity.AbstractEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Set;

@Entity
public class TrackInformationEntity extends AbstractEntity {
    @Id
    private String spotifyTrackId;

    private String spotifyAlbumId;

    @ElementCollection
    private Set<String> spotifyArtistIds;

    public String getSpotifyTrackId() {
        return spotifyTrackId;
    }

    public void setSpotifyTrackId(String spotifyTrackId) {
        this.spotifyTrackId = spotifyTrackId;
    }

    public String getSpotifyAlbumId() {
        return spotifyAlbumId;
    }

    public void setSpotifyAlbumId(String spotifyAlbumId) {
        this.spotifyAlbumId = spotifyAlbumId;
    }

    public Set<String> getSpotifyArtistIds() {
        return spotifyArtistIds;
    }

    public void setSpotifyArtistIds(Set<String> spotifyArtistIds) {
        this.spotifyArtistIds = spotifyArtistIds;
    }
}

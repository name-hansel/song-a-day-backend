package com.hanselname.songaday.spotify.exception;

import com.hanselname.songaday.common.exception.exception.ApiException;
import org.springframework.http.HttpStatus;

public class TrackNotFoundException extends ApiException {

    public TrackNotFoundException() {
        super(HttpStatus.NOT_FOUND, "SPOTIFY_TRACK_NOT_FOUND");
    }
}

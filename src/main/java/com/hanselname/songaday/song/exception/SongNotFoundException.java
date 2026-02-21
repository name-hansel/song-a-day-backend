package com.hanselname.songaday.song.exception;

import com.hanselname.songaday.common.exception.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SongNotFoundException extends ApiException {

    public SongNotFoundException() {
        super(HttpStatus.NOT_FOUND, "SONG_OF_DAY_NOT_FOUND");
    }
}

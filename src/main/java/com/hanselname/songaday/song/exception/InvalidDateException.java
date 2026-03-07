package com.hanselname.songaday.song.exception;

import com.hanselname.songaday.common.exception.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidDateException extends ApiException {

    public InvalidDateException() {
        super(HttpStatus.BAD_REQUEST, "INVALID_DATE");
    }
}

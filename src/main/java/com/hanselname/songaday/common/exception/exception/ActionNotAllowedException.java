package com.hanselname.songaday.common.exception.exception;

import org.springframework.http.HttpStatus;

public class ActionNotAllowedException extends ApiException {
    public ActionNotAllowedException() {
        super(HttpStatus.BAD_REQUEST, "ACTION_NOT_ALLOWED");
    }
}

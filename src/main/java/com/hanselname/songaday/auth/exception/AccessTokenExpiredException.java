package com.hanselname.songaday.auth.exception;

import com.hanselname.songaday.common.exception.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AccessTokenExpiredException extends ApiException {

    public AccessTokenExpiredException() {
        super(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_EXPIRED");
    }
}

package com.hanselname.songaday.auth.exception;

import com.hanselname.songaday.common.exception.exception.ApiException;
import org.springframework.http.HttpStatus;

public class RefreshTokenExpiredException extends ApiException {

    public RefreshTokenExpiredException() {
        super(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_EXPIRED");
    }
}

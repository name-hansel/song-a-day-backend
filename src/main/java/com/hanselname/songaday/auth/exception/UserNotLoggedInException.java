package com.hanselname.songaday.auth.exception;

import com.hanselname.songaday.common.exception.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UserNotLoggedInException extends ApiException {

    public UserNotLoggedInException() {
        super(HttpStatus.UNAUTHORIZED, "USER_NOT_LOGGED_IN");
    }
}

package com.hanselname.songaday.user.exception;

import com.hanselname.songaday.common.exception.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }
}

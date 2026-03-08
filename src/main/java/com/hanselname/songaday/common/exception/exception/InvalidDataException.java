package com.hanselname.songaday.common.exception.exception;

import org.springframework.http.HttpStatus;

public class InvalidDataException extends ApiException {
    public InvalidDataException(String invalidDataIdentifier) {
        super(HttpStatus.BAD_REQUEST, "INVALID_DATA_" + invalidDataIdentifier);
    }
}

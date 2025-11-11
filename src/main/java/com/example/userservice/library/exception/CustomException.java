package com.example.userservice.library.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode code;

    public CustomException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}

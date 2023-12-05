package com.example.exceptions;

public class UserConvertException extends RuntimeException{
    public UserConvertException() {
    }

    public UserConvertException(String message) {
        super(message);
    }

    public UserConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserConvertException(Throwable cause) {
        super(cause);
    }

    public UserConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

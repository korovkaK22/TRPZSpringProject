package com.example.exceptions;

public class UserEditException extends RuntimeException{
    public UserEditException() {
    }

    public UserEditException(String message) {
        super(message);
    }

    public UserEditException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserEditException(Throwable cause) {
        super(cause);
    }

    public UserEditException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

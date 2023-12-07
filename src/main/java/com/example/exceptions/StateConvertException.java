package com.example.exceptions;

public class StateConvertException extends RuntimeException{
    public StateConvertException() {
    }

    public StateConvertException(String message) {
        super(message);
    }

    public StateConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateConvertException(Throwable cause) {
        super(cause);
    }

    public StateConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

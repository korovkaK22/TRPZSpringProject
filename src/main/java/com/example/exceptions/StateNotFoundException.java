package com.example.exceptions;

public class StateNotFoundException extends RuntimeException{
    public StateNotFoundException() {
    }

    public StateNotFoundException(String message) {
        super(message);
    }

    public StateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateNotFoundException(Throwable cause) {
        super(cause);
    }

    public StateNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

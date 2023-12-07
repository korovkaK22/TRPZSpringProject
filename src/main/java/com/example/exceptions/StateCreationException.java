package com.example.exceptions;

public class StateCreationException extends RuntimeException{
    public StateCreationException() {
    }

    public StateCreationException(String message) {
        super(message);
    }

    public StateCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateCreationException(Throwable cause) {
        super(cause);
    }

    public StateCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

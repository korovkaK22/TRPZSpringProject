package com.example.server.exceptions;

public class ServerInitializationException extends Exception{
    public ServerInitializationException() {
    }

    public ServerInitializationException(String message) {
        super(message);
    }

    public ServerInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerInitializationException(Throwable cause) {
        super(cause);
    }

    public ServerInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

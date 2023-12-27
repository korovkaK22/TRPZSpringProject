package com.example.exceptions;

public class ServerUserInitializationException extends Exception{
    public ServerUserInitializationException() {
    }

    public ServerUserInitializationException(String message) {
        super(message);
    }

    public ServerUserInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerUserInitializationException(Throwable cause) {
        super(cause);
    }

    public ServerUserInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

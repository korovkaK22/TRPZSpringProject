package com.example.exceptions;

public class ServerAccessStateInitException extends Exception{
    public ServerAccessStateInitException() {
    }

    public ServerAccessStateInitException(String message) {
        super(message);
    }

    public ServerAccessStateInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerAccessStateInitException(Throwable cause) {
        super(cause);
    }

    public ServerAccessStateInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

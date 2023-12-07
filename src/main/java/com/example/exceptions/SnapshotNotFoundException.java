package com.example.exceptions;

public class SnapshotNotFoundException extends RuntimeException{
    public SnapshotNotFoundException() {
    }

    public SnapshotNotFoundException(String message) {
        super(message);
    }

    public SnapshotNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnapshotNotFoundException(Throwable cause) {
        super(cause);
    }

    public SnapshotNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

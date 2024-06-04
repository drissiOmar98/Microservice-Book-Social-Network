package com.example.feedbackserver.exception;

public class ConnectedUserNotFoundException extends RuntimeException{
    public ConnectedUserNotFoundException(String message) {
        super(message);
    }

    public ConnectedUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

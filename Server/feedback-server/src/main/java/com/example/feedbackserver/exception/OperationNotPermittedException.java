package com.example.feedbackserver.exception;

public class OperationNotPermittedException  extends RuntimeException{
    public OperationNotPermittedException() {
    }

    public OperationNotPermittedException(String message) {
        super(message);
    }
}

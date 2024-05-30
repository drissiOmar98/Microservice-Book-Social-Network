package com.example.bookserver.exception;

public class OperationNotPermittedException  extends RuntimeException{
    public OperationNotPermittedException() {
    }

    public OperationNotPermittedException(String message) {
        super(message);
    }
}

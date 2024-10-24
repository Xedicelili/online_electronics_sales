package com.test.electronic.exception;

public class VerificationFailedException extends RuntimeException{
    public VerificationFailedException(String msg){
        super(msg);
    }
}

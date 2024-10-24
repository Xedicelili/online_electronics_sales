package com.test.electronic.exception;

public class InvalidVerificationCodeException extends RuntimeException{
    public InvalidVerificationCodeException(String message){
        super(message);
    }
}

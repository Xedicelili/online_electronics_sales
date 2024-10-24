package com.test.electronic.exception;

public class PaymentProcessingException extends RuntimeException{
    public PaymentProcessingException(String message) {
        super(message);
    }
}

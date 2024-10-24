package com.test.electronic.exception;

public class OutOfStockException extends RuntimeException{
    public OutOfStockException(String message){
        super(message);

    }
}

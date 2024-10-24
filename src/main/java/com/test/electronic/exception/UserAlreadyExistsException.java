package com.test.electronic.exception;

    public class UserAlreadyExistsException extends RuntimeException{
        public UserAlreadyExistsException(String msg){
            super(msg);
        }

}

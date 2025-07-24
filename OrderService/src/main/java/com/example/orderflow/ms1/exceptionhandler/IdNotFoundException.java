package com.example.orderflow.ms1.exceptionhandler;

public class IdNotFoundException extends RuntimeException {

    public IdNotFoundException(String message) {

        super(message);
    }
}

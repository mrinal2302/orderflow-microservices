package com.example.orderservice.exceptionhandler;

public class IdNotFoundException extends RuntimeException {

    public IdNotFoundException(String message) {

        super(message);
    }
}

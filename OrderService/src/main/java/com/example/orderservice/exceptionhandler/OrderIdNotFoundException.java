package com.example.orderservice.exceptionhandler;

public class OrderIdNotFoundException extends RuntimeException {

    public OrderIdNotFoundException(String message) {

        super(message);
    }
}
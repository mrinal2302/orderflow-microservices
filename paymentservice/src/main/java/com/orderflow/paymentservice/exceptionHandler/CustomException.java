package com.orderflow.paymentservice.exceptionHandler;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}

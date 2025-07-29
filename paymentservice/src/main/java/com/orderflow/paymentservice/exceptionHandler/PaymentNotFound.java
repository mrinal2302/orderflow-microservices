package com.orderflow.paymentservice.exceptionHandler;

public class PaymentNotFound extends RuntimeException {
    public PaymentNotFound(String message) {
        super(message);
    }
}

package com.orderflow.paymentservice.exceptionHandler;

public class PaymentUpdateException extends RuntimeException {
    public PaymentUpdateException(String message) {
        super(message);
    }
}

package com.orderflow.paymentservice.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderIdNotFoundException(OrderIdNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "ORDER_ID_NOT_FOUND");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentNotFound.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFound(PaymentNotFound ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "PAYMENT_NOT_FOUND");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentUpdateException.class)
    public ResponseEntity<ErrorResponse> handlePaymentUpdateException(PaymentUpdateException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "PAYMENT_UPDATE_ERROR");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceCommunicationException.class)
    public ResponseEntity<ErrorResponse> handleServiceCommunicationException(ServiceCommunicationException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), "SERVICE_COMMUNICATION_ERROR");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse error = new ErrorResponse("An unexpected error occurred: " + ex.getMessage(), "UNEXPECTED_ERROR");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
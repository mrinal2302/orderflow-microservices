package com.orderflow.paymentservice.exceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseStatus
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderIdException(OrderIdNotFoundException ex, HttpServletRequest httpServletRequest) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), httpServletRequest.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentNotFound.class)
    public ResponseEntity<ErrorResponse> handlePaymentException(OrderIdNotFoundException ex, HttpServletRequest httpServletRequest) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), httpServletRequest.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest httpServletRequest) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), httpServletRequest.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

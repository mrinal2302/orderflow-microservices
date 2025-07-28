package com.example.orderservice.exceptionhandler;

import com.example.orderservice.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(OrderIdNotFoundException.class)

    public ResponseEntity<ErrorResponse> OrderIdNotFound(OrderIdNotFoundException idNotFoundException, HttpServletRequest httpServletRequest) {
        ErrorResponse errorResponse = new ErrorResponse(idNotFoundException.getMessage(), httpServletRequest.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ErrorResponse> anyException(Exception exception, HttpServletRequest httpServletRequest) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), httpServletRequest.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
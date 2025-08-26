package com.notification.service.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataInsufficientException.class)
    public ResponseEntity<ErrorResponse> handelDataIncompleteException(DataInsufficientException dataException , HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(dataException.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }
}

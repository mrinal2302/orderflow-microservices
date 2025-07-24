package com.example.orderflow.ms1.exceptionhandler;

import com.example.orderflow.ms1.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(IdNotFoundException.class)

    public ResponseEntity<ErrorResponse> idNotFound(IdNotFoundException idNotFoundExcep, HttpServletRequest httpServletRequest) {
        ErrorResponse errorResponse = new ErrorResponse(idNotFoundExcep.getMessage(), httpServletRequest.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<ErrorResponse> anyException(Exception exception, HttpServletRequest httpServletRequest) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), httpServletRequest.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}

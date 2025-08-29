package com.notification.service.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataInsufficientException.class)
        public ResponseEntity<ErrorResponse> handelDataIncompleteException(DataInsufficientException dataException, HttpServletRequest request){
            ErrorResponse error = new ErrorResponse(dataException.getMessage(), request.getRequestURI());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(NotificationException.class)
        public ResponseEntity<ErrorResponse> handleNotificationException(NotificationException ex, HttpServletRequest request) {
            ErrorResponse error = new ErrorResponse(ex.getMessage(), request.getRequestURI());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Optional: catch-all for other exceptions
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
            ErrorResponse error = new ErrorResponse("An unexpected error occurred", request.getRequestURI());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



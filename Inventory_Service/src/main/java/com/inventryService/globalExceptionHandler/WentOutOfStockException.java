package com.inventryService.globalExceptionHandler;

public class WentOutOfStockException extends RuntimeException{
    public WentOutOfStockException(String message) {

        super(message);
    }

}

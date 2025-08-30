package com.inventryService.globalExceptionHandler;

import com.inventryService.model.Error_Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class StockNotFoundException {
    public StockNotFoundException() {
    }

    @ExceptionHandler({WentOutOfStockException.class})
    public ResponseEntity<Error_Response> stockNoyAvailable(WentOutOfStockException woutOfStockException, HttpServletRequest httpServletRequest) {
        Error_Response errorResponse = new Error_Response(woutOfStockException.getMessage(), httpServletRequest.getRequestURI());
        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }

}

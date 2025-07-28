package com.example.orderservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
public class ErrorResponse {

    private String message;
    private String path;
    private LocalDateTime timestamped;

    public ErrorResponse(String message, String path) {
        this.message = message;
        this.path = path;
        this.timestamped = LocalDateTime.now();
    }
}
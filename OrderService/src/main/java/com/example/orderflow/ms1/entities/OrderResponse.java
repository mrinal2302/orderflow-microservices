package com.example.orderflow.ms1.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class OrderResponse {
    private long orderId;
    private String email;
    private String status;
    private String message;


}

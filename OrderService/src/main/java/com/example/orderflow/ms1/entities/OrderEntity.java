package com.example.orderflow.ms1.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id Cannot be Null")
    private long orderId;
    @NotBlank(message = "CustomerName Cannot be Blank")
    private String customerName;
    @NotBlank(message = "Product Id Cannot be Blank")
    private String productId;
    private long quantity;
    private double totalAmount;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    private String email;
    private String message;


}















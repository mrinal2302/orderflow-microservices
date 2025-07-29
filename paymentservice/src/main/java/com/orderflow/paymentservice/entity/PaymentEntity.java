package com.orderflow.paymentservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "payments")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @NotNull(message = "id should not be empty")
    private Long orderId;
    @NotNull(message = "amount should not be empty")
    private Double amount;
    @NotNull(message = "paymentMethod should not be empty")
    private String paymentMethod;
    @NotNull(message = "paymentStatus should not be empty")
    private String paymentStatus;
    @NotNull(message = "EmailAddress should not be Empty")
    private String emailAddress;

}
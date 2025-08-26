package com.orderflow.paymentservice.entity;

import com.orderflow.paymentservice.model.PaymentMethod;
import com.orderflow.paymentservice.model.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @NotNull(message = "id should not be empty")
    private Long orderId;
    @NotNull(message = "amount should not be empty")
    private Double amount;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "paymentMode should not be empty")
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "paymentStatus should not be empty")
    private PaymentStatus paymentStatus;
    @NotNull(message = "EmailAddress should not be Empty")
    private String emailAddress;

}

package com.example.orderservice.entities;

import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.enums.PaymentMode;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
/*@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor*/
@Builder
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @NotBlank(message = "Customer Name cannot be blank")
    private String customerName;

    @NotBlank(message = "Product Id cannot be blank")
    private String productId;

    private String productName;

    @NotNull(message = "Quantity cannot be null")
    private long quantity;

    @NotNull(message = "Total Amount cannot be null")
    private double totalAmount;

    // ✅ Now using ENUM instead of String
    @Enumerated(EnumType.STRING)
    private OrderStatus status;   // PENDING, SUCCESS, FAILURE

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    private String message; // e.g. "Order placed successfully", "Payment failed"

    // ✅ Enum instead of String for safer payment mode handling
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;  // UPI, CREDIT_CARD, DEBIT_CARD

    public OrderEntity(long orderId, String customerName, String productId, String productName, long quantity,
                       double totalAmount, OrderStatus status, LocalDate orderDate, String email,
                       String message, PaymentMode paymentMode) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.email = email;
        this.message = message;
        this.paymentMode = paymentMode;
    }

    public OrderEntity() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }
}

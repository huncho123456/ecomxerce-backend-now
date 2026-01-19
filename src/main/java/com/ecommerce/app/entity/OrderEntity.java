package com.ecommerce.app.entity;

import com.ecommerce.app.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "guest_id", nullable = false)
    private UUID guestId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "payment_intent_id")
    private String paymentIntentId; // To link this order to a transaction in Stripe/PayPal

    @Column(name = "total_amount", nullable = false )
    private Integer totalAmount;

    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "tracking_token", unique = true, nullable = false)
    private String trackingToken;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}


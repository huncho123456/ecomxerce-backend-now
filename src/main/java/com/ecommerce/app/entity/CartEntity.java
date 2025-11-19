package com.ecommerce.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "carts")


public class CartEntity {
    @Id
    private UUID guest_id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItemEntity> items = new ArrayList<>();



    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}

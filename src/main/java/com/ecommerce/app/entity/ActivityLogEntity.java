package com.ecommerce.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "activity_logs")
public class ActivityLogEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "guest_id", nullable = false)
    private UUID guestId;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "target", nullable = false)
    private String target;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private Instant timestamp = Instant.now();
}



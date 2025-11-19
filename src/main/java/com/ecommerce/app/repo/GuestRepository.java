package com.ecommerce.app.repo;

import com.ecommerce.app.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, UUID> {
}

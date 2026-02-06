package com.ecommerce.app.repo;

import com.ecommerce.app.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {

    CartEntity findByGuestId(UUID guestId);
    CartEntity findByUserId(Long userId);

}

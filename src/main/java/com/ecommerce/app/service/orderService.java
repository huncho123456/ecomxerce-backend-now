package com.ecommerce.app.service;

import com.ecommerce.app.entity.OrderEntity;
import com.ecommerce.app.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class orderService {

    private final OrderRepository repo;

    public OrderEntity createOrder(UUID guestId, String email, int total) {
        OrderEntity o = new OrderEntity();
        o.setGuestId(guestId);
        o.setEmail(email);
        o.setTotalAmount(total);
        o.setTrackingToken(generateToken());
        return repo.save(o);
    }


    public OrderEntity trackOrder(String token) {
        return repo.findByTrackingToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order not found for tracking token: " + token
                ));
    }

    private String generateToken() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}

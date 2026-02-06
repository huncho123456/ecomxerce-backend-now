package com.ecommerce.app.service;

import com.ecommerce.app.entity.*;
import com.ecommerce.app.repo.CartRepository;
import com.ecommerce.app.repo.GuestRepository;
import com.ecommerce.app.repo.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class cartService {
    private final ProductRepository productRepo;
    private final CartRepository cartRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void migrateGuestCartToUser(UUID guestId, Long userId) {
        CartEntity guestCart = cartRepository.findByGuestId(guestId);
        if (guestCart == null) return;

        // Check if user already has a cart
        CartEntity userCart = cartRepository.findByUserId(userId);
        UserEntity userRef = entityManager.getReference(UserEntity.class, userId);

        if (userCart == null) {
            // Promote guest cart to user
            guestCart.setUser(userRef);
            guestCart.setGuestId(null);
            cartRepository.save(guestCart);
        } else {
            // Merge items from guest cart into existing user cart
            for (CartItemEntity item : new ArrayList<>(guestCart.getItems())) {
                Optional<CartItemEntity> existing = userCart.getItems().stream()
                        .filter(i -> i.getProduct().getId().equals(item.getProduct().getId()))
                        .findFirst();

                if (existing.isPresent()) {
                    existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
                } else {
                    item.setCart(userCart);
                    userCart.getItems().add(item);
                }
            }
            cartRepository.delete(guestCart);
            cartRepository.save(userCart);
        }
    }

    // 🛒 Add product to guest's cart
    @Transactional
    public CartEntity addToCart(UUID guestId, Long productId, int qty) {
        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Find cart by guestId
        CartEntity cart = cartRepository.findByGuestId(guestId);
        if (cart == null) {
            cart = new CartEntity();
            cart.setGuestId(guestId);
            cart.setItems(new ArrayList<>());
            cart.setCreatedAt(LocalDateTime.now());
        }

        // Add or update cart item
        Optional<CartItemEntity> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + qty);
        } else {
            CartItemEntity newItem = new CartItemEntity();
            newItem.setProduct(product);
            newItem.setQuantity(qty);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        cart.setCreatedAt(LocalDateTime.now());
        cart = cartRepository.save(cart);

        return cart;
    }


    // 🧾 Get cart by guest ID
    public CartEntity getCart(UUID guestId) {
        return cartRepository.findById(guestId)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setGuestId(guestId);
                    newCart.setItems(new ArrayList<>());
                    newCart.setCreatedAt(LocalDateTime.now());
                    return cartRepository.save(newCart);
                });
    }

    // 🗑️ Clear cart
    public void clearCart(UUID guestId) {
        cartRepository.deleteById(guestId);
    }
}
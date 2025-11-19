package com.ecommerce.app.service;

import com.ecommerce.app.entity.CartEntity;
import com.ecommerce.app.entity.CartItemEntity;
import com.ecommerce.app.entity.GuestEntity;
import com.ecommerce.app.entity.ProductEntity;
import com.ecommerce.app.repo.CartRepository;
import com.ecommerce.app.repo.GuestRepository;
import com.ecommerce.app.repo.ProductRepository;
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

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    // 🛒 Add product to guest's cart
    public CartEntity addToCart(UUID guestId, Long productId, int qty) {
        if (guestId == null) {
            throw new RuntimeException("guestId is required");
        }

        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Get existing cart or create a new one
        CartEntity cart = cartRepo.findById(guestId)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setGuest_id(guestId);
                    newCart.setItems(new ArrayList<>());
                    newCart.setUpdatedAt(LocalDateTime.now());
                    return newCart;
                });

        // Check if the product is already in the cart
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

        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepo.save(cart);
    }

    // 🧾 Get cart by guest ID
    public CartEntity getCart(UUID guestId) {
        return cartRepo.findById(guestId)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setGuest_id(guestId);
                    newCart.setItems(new ArrayList<>());
                    newCart.setUpdatedAt(LocalDateTime.now());
                    return cartRepo.save(newCart);
                });
    }

    // 🗑️ Clear cart
    public void clearCart(UUID guestId) {
        cartRepo.deleteById(guestId);
    }
}
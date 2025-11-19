package com.ecommerce.app.controller;

import com.ecommerce.app.entity.CartEntity;
import com.ecommerce.app.service.cartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class cartController {

    private final cartService cartService;

    // 🛒 Add item to cart
    @PostMapping("/add")
    public CartEntity addToCart(
            @CookieValue(value = "guest_id", required = false) UUID guestId,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int qty,
            HttpServletResponse response) {

        // ✅ If guest_id cookie doesn't exist, create one
        if (guestId == null) {
            guestId = UUID.randomUUID();
            Cookie cookie = new Cookie("guest_id", guestId.toString());
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // set to true if using HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
            response.addCookie(cookie);
            System.out.println("🆕 New guest_id created: " + guestId);
        } else {
            System.out.println("✅ Existing guest_id used: " + guestId);
        }

        // Call service
        return cartService.addToCart(guestId, productId, qty);
    }



    // Get current cart
    @GetMapping
    public CartEntity getCart(@CookieValue(value = "guest_id", required = false) UUID guestId,
                              HttpServletResponse response) {
        if (guestId == null) {
            guestId = UUID.randomUUID();
            Cookie cookie = new Cookie("guest_id", guestId.toString());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);
        }

        return cartService.getCart(guestId);
    }

    // Clear cart
    @DeleteMapping("/clear")
    public String clearCart(@CookieValue(value = "guest_id") UUID guestId) {
        cartService.clearCart(guestId);
        return "Cart cleared successfully";
    }
}

package com.ecommerce.app.controller;

import com.ecommerce.app.entity.OrderEntity;
import com.ecommerce.app.service.orderService;
import com.ecommerce.app.service.cartService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class checkOutController {

    private final orderService orderService;
    private final cartService cartService;

    /**
     * Checkout as a guest user.
     * - Creates an order for the given guest_id (from cookie)
     * - If no cookie exists, generate one and attach to response
     */
    @PostMapping("/guest")
    public OrderEntity checkout(@CookieValue(value = "guest_id", required = false) UUID guestId,
                                @RequestBody CheckoutRequest req,
                                HttpServletResponse response) {

        // If no guest_id cookie exists, assign one
        if (guestId == null) {
            guestId = UUID.randomUUID();
            Cookie cookie = new Cookie("guest_id", guestId.toString());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
            response.addCookie(cookie);
        }

        // Create order from guest cart
        OrderEntity order = orderService.createOrder(guestId, req.getEmail(), req.getTotalAmount());

        // Clear guest's cart after checkout
        cartService.clearCart(guestId);

        return order;
    }

    // DTO for request body
    @Data
    static class CheckoutRequest {
        @Email
        private String email;

        @NotNull
        private Integer totalAmount;
    }
}

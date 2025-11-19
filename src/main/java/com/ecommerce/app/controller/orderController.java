package com.ecommerce.app.controller;

import com.ecommerce.app.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.app.service.orderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class orderController {
    private final orderService orderService;

    @GetMapping("/track/{token}")
    public OrderEntity track(@PathVariable String token) {
        log.info("Order tracking requested for token {}", token);

        return orderService.trackOrder(token);
    }
}

package com.ecommerce.app.payments;

import com.ecommerce.app.payments.paystack.PaystackResponse;
import com.ecommerce.app.payments.paystack.PaystackService;
import com.ecommerce.app.payments.paystack.PaystackVerifyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaystackService paystackService;

    @PostMapping("/initialize")
    public ResponseEntity<PaystackResponse> initialize(@RequestBody PaymentRequest request) {
        log.info("initialize" + request.getEmail());
        PaystackResponse response = paystackService.initPayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify/{reference}")
    public ResponseEntity<PaystackVerifyResponse> verify(@PathVariable String reference) {
        PaystackVerifyResponse response = paystackService.verifyPayment(reference);

        if (response != null && response.isStatus()) {
            String paymentStatus = response.getData().getStatus();

            if ("success".equalsIgnoreCase(paymentStatus)) {
                //  Optional: Validate against your order
                // Order order = orderRepository.findByReference(reference);
                // if (order != null && order.getAmountInKobo().equals(response.getData().getAmount())) {
                //     order.setStatus(OrderStatus.PAID);
                //     orderRepository.save(order);
                // }

                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.badRequest().body(response);
    }
}

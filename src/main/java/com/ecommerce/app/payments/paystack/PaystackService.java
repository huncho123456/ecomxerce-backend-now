package com.ecommerce.app.payments.paystack;

import com.ecommerce.app.payments.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // Corrected Import
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaystackService {

    @Value("${paystack.secret.key}")
    private String secretKey;

    @Value("${paystack.base.url}")
    private String baseUrl;

    public PaystackResponse initPayment(PaymentRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + secretKey);

        HttpEntity<PaymentRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject(
                baseUrl + "/transaction/initialize",
                entity,
                PaystackResponse.class
        );
    }

    public PaystackVerifyResponse verifyPayment(String reference) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<PaystackVerifyResponse> response = restTemplate.exchange(
                baseUrl + "/transaction/verify/" + reference,
                HttpMethod.GET,
                entity,
                PaystackVerifyResponse.class
        );

        return response.getBody();
    }
}
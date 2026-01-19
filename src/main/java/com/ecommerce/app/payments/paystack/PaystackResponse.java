package com.ecommerce.app.payments.paystack;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaystackResponse {
    private boolean status;
    private String message;
    private Data data;

    @lombok.Data
    public static class Data {
        private String authorization_url;
        private String access_code;
        private String reference;
        private String status;
    }
}

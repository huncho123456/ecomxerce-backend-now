package com.ecommerce.app.payments.paystack;

import lombok.Data;

@Data
public class PaystackVerifyResponse {
    private boolean status;
    private String message;
    private VerifyData data;

    @Data
    public static class VerifyData {
        private String status;          // "success"
        private String reference;
        private Long amount;           // in kobo
        private String paid_at;
        private String channel;
        private Customer customer;
        private Metadata metadata;

        @Data
        public static class Customer {
            private String email;
            private String first_name;
            private String last_name;
        }

        @Data
        public static class Metadata {
            // e.g., custom fields like order_id
        }
    }
}
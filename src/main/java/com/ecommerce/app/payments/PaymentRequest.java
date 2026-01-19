package com.ecommerce.app.payments;

import lombok.Data;

@Data
public class PaymentRequest {
    private String amount; // In kobo (multiply Naira by 100)
    private String email;
    private String callback_url;
}

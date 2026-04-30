package com.capg.rechargenova.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    @Value("${razorpay.key.id:}")
    private String keyId;

    @Value("${razorpay.key.secret:}")
    private String keySecret;

    @Bean
    public RazorpayClient razorpayClient() throws RazorpayException {
        if (keyId == null || keyId.isEmpty() || keySecret == null || keySecret.isEmpty()) {
            System.err.println("CRITICAL: Razorpay keys are missing! create-order will fail.");
            // Return a dummy client or handle it - for now, just let it try to create
            // but we've logged the issue.
        }
        return new RazorpayClient(
            (keyId != null ? keyId : "").trim(), 
            (keySecret != null ? keySecret : "").trim()
        );
    }
}

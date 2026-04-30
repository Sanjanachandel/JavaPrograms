package com.capg.rechargenova.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.capg.rechargenova.dto.PaymentRequest;
import com.capg.rechargenova.dto.PaymentResponse;

@FeignClient(
	    name = "payment-service",
	    url = "http://payment-service:8084"
	)
	public interface PaymentClient {

	    @PostMapping("/api/payments")
	    PaymentResponse processPayment(@RequestBody PaymentRequest request);
	}
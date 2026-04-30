package com.capg.rechargenova.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-service", path = "/api/notifications")
public interface NotificationClient {

    @PostMapping("/send-otp")
    void sendOtp(@RequestParam("email") String email, @RequestParam("otp") String otp);
}

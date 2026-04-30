package com.capg.rechargenova.service;

import com.capg.rechargenova.dto.RechargeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "recharge-service")
public interface RechargeFeignClient {
    @GetMapping("/recharges/{id}")
    RechargeResponse getRechargeById(@PathVariable("id") Long id);
}

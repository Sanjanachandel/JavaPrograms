package com.capg.rechargenova.service;

import com.capg.rechargenova.dto.OperatorDto;
import com.capg.rechargenova.dto.PlanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "operator-service")
public interface OperatorFeignClient {
    @GetMapping("/operators/{id}")
    OperatorDto getOperatorById(@PathVariable("id") Long id);

    @GetMapping("/operators/plans/{id}")
    PlanDto getPlanById(@PathVariable("id") Long id);
}

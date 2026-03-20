package com.capg.springboot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.capg.springboot.dto.UserDTO;

@FeignClient(name = "login-service", url = "http://localhost:9090")
public interface LoginClient {

    @GetMapping("/users/getUser/{id}")
    UserDTO getUser(@PathVariable("id") String id);
}
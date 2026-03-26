package com.capg.rechargenova.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.capg.rechargenova.dto.UserDto;

@FeignClient(
	    name = "user-service",
	    url = "http://user-service:8082"
	)
	public interface UserClient {

	    @GetMapping("/users/{id}")
	    UserDto getUserById(@PathVariable("id") Long id);
	}
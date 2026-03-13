package com.capg.springboot;

import com.capg.springboot.controller.OrderController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients (basePackages="com.capg.springboot.controller")
@SpringBootApplication
public class ECommerceOrderServiceApplication {

    private final OrderController orderController;

    ECommerceOrderServiceApplication(OrderController orderController) {
        this.orderController = orderController;
    }

	public static void main(String[] args) {
		SpringApplication.run(ECommerceOrderServiceApplication.class, args);
	     System.out.println("SpringBoot Product Server Start");
	}

}

package com.demoorg.demataccountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class DemataccountserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemataccountserviceApplication.class, args);
		System.out.println("Demataccount service server started...");
	}

}

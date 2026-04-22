package com.capg.rechargenova;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OperatorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OperatorServiceApplication.class, args);
        System.out.println("Operator service Server Started");
        
    }
}

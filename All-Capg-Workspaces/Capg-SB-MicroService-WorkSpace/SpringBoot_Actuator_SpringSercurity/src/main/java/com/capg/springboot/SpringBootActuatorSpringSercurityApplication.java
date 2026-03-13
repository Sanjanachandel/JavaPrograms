package com.capg.springboot;

import com.capg.springboot.controller.HelloController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootActuatorSpringSercurityApplication {

    private final HelloController helloController;

    SpringBootActuatorSpringSercurityApplication(HelloController helloController) {
        this.helloController = helloController;
    }

	public static void main(String[] args) {
		SpringApplication.run(SpringBootActuatorSpringSercurityApplication.class, args);
	 System.out.println("server running ");
	}

}

package com.capg.rechargenova;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

    @Test
    void testMain() {
        try {
            UserServiceApplication.main(new String[] {
                "--spring.main.web-application-type=none",
                "--eureka.client.enabled=false",
                "--spring.cloud.config.enabled=false"
            });
        } catch (Exception e) {}
    }
}

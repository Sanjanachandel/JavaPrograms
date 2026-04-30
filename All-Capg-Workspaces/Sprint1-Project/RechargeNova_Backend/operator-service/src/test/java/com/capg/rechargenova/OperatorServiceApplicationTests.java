package com.capg.rechargenova;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OperatorServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testMain() {
		try (var mocked = org.mockito.Mockito.mockStatic(org.springframework.boot.SpringApplication.class)) {
			mocked.when(() -> org.springframework.boot.SpringApplication.run(OperatorServiceApplication.class, new String[] {}))
					.thenReturn(null);
			OperatorServiceApplication.main(new String[] {});
			mocked.verify(() -> org.springframework.boot.SpringApplication.run(OperatorServiceApplication.class, new String[] {}));
		}
	}
}

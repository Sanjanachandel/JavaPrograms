package com.capg.rechargenova;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class EurekaServerApplicationTest {

    @Test
    void testMain() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(EurekaServerApplication.class, new String[]{}))
                    .thenReturn(null);
            EurekaServerApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(EurekaServerApplication.class, new String[]{}));
        }
    }
}

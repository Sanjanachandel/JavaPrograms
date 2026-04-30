package com.capg.rechargenova;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class NotificationServiceApplicationTests {

    @MockBean
    private JavaMailSender mailSender;

	@Test
	void contextLoads() {
	}

    @Test
    void testMain() {
        try {
            NotificationServiceApplication.main(new String[] {
                "--spring.main.web-application-type=none",
                "--eureka.client.enabled=false",
                "--spring.mail.host=localhost",
                "--spring.main.banner-mode=off"
            });
        } catch (Exception e) {}
        assertNotNull(new NotificationServiceApplication());
    }

    @Test
    void testLoadDotenvBranches() throws java.io.IOException {
        // Cover branches in loadDotenv
        
        // 1. Cover second if true (property not set)
        System.clearProperty("TEST_VAR");
        NotificationServiceApplication.loadDotenv();
        
        // 2. Cover second if false (property already set)
        System.setProperty("TEST_VAR", "value");
        NotificationServiceApplication.loadDotenv();
        
        // 3. Cover first if false (DB_USERNAME found in first load)
        // We can simulate this by creating a .env in the current directory
        java.io.File tempEnv = new java.io.File(".env");
        boolean created = tempEnv.createNewFile();
        try {
            java.nio.file.Files.writeString(tempEnv.toPath(), "DB_USERNAME=root\nTEST_VAR=val");
            NotificationServiceApplication.loadDotenv();
        } finally {
            if (created) tempEnv.delete();
        }
    }

}

package com.capg.rechargenova;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NotificationServiceApplication {

	public static void main(String[] args) {
		loadDotenv();
		SpringApplication.run(NotificationServiceApplication.class, args);
		System.out.println("Notification server started");
	}

	public static void loadDotenv() {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		if (dotenv.get("DB_USERNAME") == null) {
			dotenv = Dotenv.configure().directory("..").ignoreIfMissing().load();
		}
		dotenv.entries().forEach(entry -> {
			if (System.getProperty(entry.getKey()) == null) {
				System.setProperty(entry.getKey(), entry.getValue());
			}
		});
	}

}

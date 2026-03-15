package com.capg.springboot.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {

    // Queue name
    public static final String QUEUE_NAME = "demo_queue";

    // Bean to create queue
    @Bean
    public Queue queue() {

        // durable = true means queue will survive server restart
        return new Queue(QUEUE_NAME, true);
    }
}
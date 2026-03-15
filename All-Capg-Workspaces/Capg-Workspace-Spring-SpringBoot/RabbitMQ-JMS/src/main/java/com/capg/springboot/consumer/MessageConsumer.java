package com.capg.springboot.consumer;



import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.capg.springboot.config.RabbitMQConfig;

@Component
public class MessageConsumer {

    // RabbitListener listens to queue
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {

        // Print received message
        System.out.println("Received Message : " + message);
    }
}
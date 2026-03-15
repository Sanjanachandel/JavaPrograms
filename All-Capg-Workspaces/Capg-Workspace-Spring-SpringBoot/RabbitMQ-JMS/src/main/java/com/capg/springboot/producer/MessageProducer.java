package com.capg.springboot.producer;

 

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.capg.springboot.config.RabbitMQConfig;

@RestController
@RequestMapping("/api")
public class MessageProducer {

    // RabbitTemplate is used to send message
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send/{msg}")
    public String sendMessage(@PathVariable String msg) {

        // Send message to queue
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, msg);

        return "Message Sent : " + msg;
    }
}
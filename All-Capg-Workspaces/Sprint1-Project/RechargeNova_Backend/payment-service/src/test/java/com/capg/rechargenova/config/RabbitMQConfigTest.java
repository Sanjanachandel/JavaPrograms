package com.capg.rechargenova.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class RabbitMQConfigTest {

    private final RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();

    @Test
    void testQueueBean() {
        Queue queue = rabbitMQConfig.queue();
        assertNotNull(queue);
    }

    @Test
    void testExchangeBean() {
        TopicExchange exchange = rabbitMQConfig.exchange();
        assertNotNull(exchange);
    }

    @Test
    void testBindingBean() {
        Queue queue = rabbitMQConfig.queue();
        TopicExchange exchange = rabbitMQConfig.exchange();
        Binding binding = rabbitMQConfig.binding(queue, exchange);
        assertNotNull(binding);
    }

    @Test
    void testConverterBean() {
        MessageConverter converter = rabbitMQConfig.converter();
        assertNotNull(converter);
    }

    @Test
    void testTemplateBean() {
        ConnectionFactory mockConnectionFactory = mock(ConnectionFactory.class);
        RabbitTemplate template = (RabbitTemplate) rabbitMQConfig.template(mockConnectionFactory);
        assertNotNull(template);
        assertNotNull(template.getMessageConverter());
    }
}

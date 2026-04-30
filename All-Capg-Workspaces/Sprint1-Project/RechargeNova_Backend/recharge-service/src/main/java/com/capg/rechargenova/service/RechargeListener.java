package com.capg.rechargenova.service;

import com.capg.rechargenova.config.RabbitMQConfig;
import com.capg.rechargenova.dto.PaymentEvent;
import com.capg.rechargenova.repository.RechargeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RechargeListener {

    private static final Logger logger = LogManager.getLogger(RechargeListener.class);

    @Autowired
    private RechargeRepository rechargeRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumePaymentEvent(PaymentEvent event) {
        logger.info("Received payment event for Recharge ID: {} with status: {}", 
                    event.getRechargeId(), event.getStatus());

        if (event.getRechargeId() != null && "SUCCESS".equalsIgnoreCase(event.getStatus())) {
            rechargeRepository.findById(event.getRechargeId()).ifPresent(recharge -> {
                recharge.setStatus("SUCCESS");
                rechargeRepository.save(recharge);
                logger.info("Recharge ID: {} status updated to SUCCESS", recharge.getId());
            });
        }
    }
}

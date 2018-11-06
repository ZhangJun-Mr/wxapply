package com.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("rabbitmq")
public class RabbitMQController {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    Receiver receiver;
    @PostMapping("/productMessage")
    public String productMessage() throws InterruptedException {
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.TOPIC_EXCHANGE_NAME, "foo.bar.baz", "Hello from RabbitMQ!");
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        return null;
    }
}

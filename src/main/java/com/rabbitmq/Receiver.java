package com.rabbitmq;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @author Administrator
 */
@Component
public class Receiver {
    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println("Received < " + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch(){
        return latch;
    }

}

package com.example.facilityservice.rabbithole;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.printf("Received <%s>\n", message);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}

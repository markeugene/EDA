package com.example.eda;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.UUID;

@EnableKafka
public class KafkaConsumer {
    private final int rangeMin = 1;
    private final int rangeMax = 50;


    @KafkaListener(groupId = "test-group", topics = "paymentFailed")
    public void paymentFailed(String orderId) {
        rollbackOrderCreationById(UUID.fromString(orderId));
    }

    @KafkaListener(groupId = "test-group", topics = "paymentCompleted")
    public void paymentCompleted(String orderId) {
        setPaidStatusForOrder(UUID.fromString(orderId));
    }


    private void rollbackOrderCreationById(UUID orderId) {
        return;
    }

    private void setPaidStatusForOrder(UUID orderId) {
        return;
    }
}

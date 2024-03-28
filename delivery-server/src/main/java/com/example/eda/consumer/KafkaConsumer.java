package com.example.eda.consumer;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.UUID;

@EnableKafka
public class KafkaConsumer {

    @KafkaListener(groupId = "test-group", topics = "paymentCompleted")
    public void paymentCompleted(String message) {
        notifyCouriers(UUID.fromString(message));
    }

    private void notifyCouriers(UUID orderId) {
        return;
    }
}

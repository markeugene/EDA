package com.example.eda.producer;


import com.example.eda.event.OrderCreatedEvent;
import com.example.eda.event.OrderUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreated(OrderCreatedEvent orderCreatedEvent) {
        kafkaTemplate.send("ORDER_CREATED",orderCreatedEvent);
    }

    public void sendOrderUpdated(OrderUpdatedEvent orderUpdatedEvent) {
        kafkaTemplate.send("ORDER_UPDATED",orderUpdatedEvent);
    }

    public void sendPaymentCompleted(String orderId) {
        kafkaTemplate.send("paymentCompleted",orderId);
    }

    public void sendPaymentFailed(String orderId) {
        kafkaTemplate.send("paymentFailed",orderId);
    }


}

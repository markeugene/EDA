package com.example.eda.producer;


import com.example.eda.enums.EventType;
import com.example.eda.event.OrderCreatedEvent;
import com.example.eda.event.OrderUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CompletableFuture<SendResult<String, Object>> sendOrderCreated(OrderCreatedEvent orderCreatedEvent) {
        return kafkaTemplate.send(EventType.ORDER_CREATED.getName(), orderCreatedEvent);
    }

    public CompletableFuture<SendResult<String, Object>> sendOrderUpdated(OrderUpdatedEvent orderUpdatedEvent) {
        return kafkaTemplate.send(EventType.ORDER_UPDATED.getName(), orderUpdatedEvent);
    }

}

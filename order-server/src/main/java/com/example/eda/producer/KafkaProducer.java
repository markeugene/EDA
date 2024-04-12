package com.example.eda.producer;


import com.example.eda.event.OrderCreatedEvent;
import com.example.eda.event.OrderUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CompletableFuture<SendResult<String, Object>> sendOrderCreated(OrderCreatedEvent orderCreatedEvent) {
        return kafkaTemplate.send("ORDER_CREATED",orderCreatedEvent);
    }

    public CompletableFuture<SendResult<String, Object>> sendOrderUpdated(OrderUpdatedEvent orderUpdatedEvent) {
        return kafkaTemplate.send("ORDER_UPDATED",orderUpdatedEvent);
    }

}

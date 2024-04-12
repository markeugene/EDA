package com.example.eda.consumer;

import com.example.eda.event.OrderCreatedEvent;
import com.example.eda.event.OrderUpdatedEvent;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.UUID;

@EnableKafka
@Setter
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

    @KafkaListener(groupId = "test-group", topics = "ORDER_CREATED")
    public void orderCreated(OrderCreatedEvent orderEvent) {
        /*orderWriteReadSyncService.syncOrderCreate(orderEvent);*/
    }

    @KafkaListener(groupId = "test-group", topics = "ORDER_UPDATED")
    public void orderCreated(OrderUpdatedEvent orderEvent) {
        /*orderWriteReadSyncService.syncOrderUpdate(orderEvent);*/
    }


    private void rollbackOrderCreationById(UUID orderId) {
        return;
    }

    private void setPaidStatusForOrder(UUID orderId) {
        return;
    }


}

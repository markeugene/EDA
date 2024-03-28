package com.example.eda.consumer;

import com.example.eda.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Random;

@EnableKafka
public class KafkaConsumer {
    private final int rangeMin = 1;
    private final int rangeMax = 50;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(groupId = "test-group", topics = "orderCreated")
    public void orderCompleted(Order createdOrder) {
        Random r = new Random();
        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();

        if (createdOrder.totalCost() > randomValue) {
            kafkaTemplate.send("paymentFailed", createdOrder.id().toString());
        } else {
            kafkaTemplate.send("paymentCompleted", createdOrder.id().toString());
        }
    }
}

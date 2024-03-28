package com.example.eda.controller;

import com.example.eda.CreateOrder;
import com.example.eda.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody CreateOrder orderToCreate) throws JsonProcessingException {

        if (orderToCreate.quantity() < 1) {
            return ResponseEntity.badRequest().build();
        }
        var createdOrder = new Order(UUID.randomUUID(), orderToCreate.item(),
                orderToCreate.quantity(), orderToCreate.totalCost(),
                orderToCreate.orderDestination(), UUID.randomUUID());
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("orderCreated", createdOrder);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + "message" +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        "message" + "] due to : " + ex.getMessage());
            }
        });

        return ResponseEntity.ok("Success");
    }
}

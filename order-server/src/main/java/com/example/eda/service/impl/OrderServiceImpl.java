package com.example.eda.service.impl;

import com.example.eda.CreateOrder;
import com.example.eda.UpdateOrder;
import com.example.eda.event.OrderCreatedEvent;
import com.example.eda.event.OrderUpdatedEvent;
import com.example.eda.model.Order;
import com.example.eda.model.OrderEvent;
import com.example.eda.repository.OrderEventRepository;
import com.example.eda.service.OrderReadService;
import com.example.eda.service.OrderWriteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderWriteService, OrderReadService {
    private final OrderEventRepository orderEventRepository;
    private final ObjectMapper objectMapper;
    @Autowired
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderServiceImpl(OrderEventRepository orderEventRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderEventRepository = orderEventRepository;
        this.kafkaTemplate = kafkaTemplate;
        objectMapper = new ObjectMapper();
    }

    @Override
    @SneakyThrows
    public void createOrder(CreateOrder createOrder) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(ThreadLocalRandom.current().nextLong(), createOrder.item(), "CREATED", createOrder.totalCost(), createOrder.orderDestination(), createOrder.userId());
        OrderEvent orderEvent = new OrderEvent(orderCreatedEvent.getId().toString(), "ORDER_CREATED", objectMapper.writeValueAsString(orderCreatedEvent));
        orderEventRepository.save(orderEvent);
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("ORDER_CREATED", orderCreatedEvent);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + "message" +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        "message" + "] due to : " + ex.getMessage());
            }
        });

    }

    @Override
    @SneakyThrows
    public void updateOrder(UpdateOrder updateOrder) {
        OrderUpdatedEvent orderUpdatedEvent = new OrderUpdatedEvent(updateOrder.id(), updateOrder.status());
        Optional<OrderEvent> orderEvent = orderEventRepository.findFirstByAggregateIdOrderByAggregateIdDesc(updateOrder.id().toString());

        orderEvent.ifPresent(oldOrderEvent -> {
            try {
                OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(oldOrderEvent.getEventData(), OrderCreatedEvent.class);
                orderCreatedEvent.setStatus(updateOrder.status());

                OrderEvent newOrderEvent = new OrderEvent(oldOrderEvent.getAggregateId(), "ORDER_UPDATED", objectMapper.writeValueAsString(orderUpdatedEvent));

                orderEventRepository.save(newOrderEvent);

                CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("ORDER_UPDATED", orderUpdatedEvent);
                future.whenComplete((result, ex) -> {
                    if (ex == null) {
                        System.out.println("Sent message=[" + "message" +
                                "] with offset=[" + result.getRecordMetadata().offset() + "]");
                    } else {
                        System.out.println("Unable to send message=[" +
                                "message" + "] due to : " + ex.getMessage());
                    }
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public Order getOrderById(String orderId) {
        List<OrderEvent> allByAggregateId = orderEventRepository.findAllByAggregateId(orderId);
        Order orderToReturn = new Order();
        AtomicReference<Object> specificEvent = new AtomicReference<>(new Object());
        orderToReturn.setId(Long.valueOf(orderId));
        allByAggregateId.forEach(event -> {
            if (event.getEventType().equals("ORDER_CREATED")) {
                try {
                    specificEvent.set(objectMapper.readValue(event.getEventData(), OrderCreatedEvent.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                OrderCreatedEvent createdOrder = (OrderCreatedEvent) specificEvent.get();
                orderToReturn.setOrderDestination(createdOrder.getOrderDestination());
                orderToReturn.setStatus(createdOrder.getStatus());
                orderToReturn.setItem(createdOrder.getItem());
                orderToReturn.setTotalCost(createdOrder.getTotalCost());
                orderToReturn.setUserId(createdOrder.getUserId());
            }
            if (event.getEventType().equals("ORDER_UPDATED")) {
                try {
                    specificEvent.set(objectMapper.readValue(event.getEventData(), OrderUpdatedEvent.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                OrderUpdatedEvent orderUpdatedEvent= (OrderUpdatedEvent) specificEvent.get();
                orderToReturn.setStatus(orderUpdatedEvent.getStatus());
            }
        });

        return orderToReturn;
    }
}

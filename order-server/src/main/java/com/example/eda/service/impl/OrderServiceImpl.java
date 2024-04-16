package com.example.eda.service.impl;

import com.example.eda.cqrs.command.CancelOrderCommand;
import com.example.eda.cqrs.command.CreateOrderCommand;
import com.example.eda.cqrs.command.UpdateOrderCommand;
import com.example.eda.cqrs.projection.OrderProjection;
import com.example.eda.cqrs.query.OrderByIdQuery;
import com.example.eda.enums.EventType;
import com.example.eda.enums.OrderStatus;
import com.example.eda.event.OrderCancelledEvent;
import com.example.eda.event.OrderCreatedEvent;
import com.example.eda.event.OrderEvent;
import com.example.eda.event.OrderUpdatedEvent;
import com.example.eda.model.Order;
import com.example.eda.producer.KafkaProducer;
import com.example.eda.repository.OrderEventRepository;
import com.example.eda.service.OrderReadService;
import com.example.eda.service.OrderWriteService;
import com.example.eda.util.OrderEventUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderWriteService, OrderReadService {
    private final OrderEventRepository orderEventRepository;
    private final OrderProjection orderProjection;
    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;
    private final OrderEventUtility orderEventUtility;


    @Override
    @SneakyThrows
    public void createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(ThreadLocalRandom.current().nextLong(), createOrderCommand.item(), OrderStatus.CREATED.getName(), createOrderCommand.totalCost(), createOrderCommand.orderDestination(), createOrderCommand.userId());
        OrderEvent orderEvent = new OrderEvent(orderCreatedEvent.getId().toString(), EventType.ORDER_CREATED.getName(), objectMapper.writeValueAsString(orderCreatedEvent));
        orderEventRepository.save(orderEvent);
        CompletableFuture<SendResult<String, Object>> future = kafkaProducer.sendOrderCreated(orderCreatedEvent);
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
    public void updateOrder(UpdateOrderCommand updateOrderCommand) {
        OrderUpdatedEvent orderUpdatedEvent = new OrderUpdatedEvent(updateOrderCommand.id(), updateOrderCommand.status(), updateOrderCommand.orderDestination());
        Optional<OrderEvent> orderEvent = orderEventRepository.findFirstByAggregateIdOrderByAggregateIdDesc(orderUpdatedEvent.getId().toString());

        orderEvent.ifPresent(oldOrderEvent -> {
            try {
                OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(oldOrderEvent.getEventData(), OrderCreatedEvent.class);
                orderCreatedEvent.setStatus(updateOrderCommand.status());

                OrderEvent newOrderEvent = new OrderEvent(oldOrderEvent.getAggregateId(), EventType.ORDER_UPDATED.getName(), objectMapper.writeValueAsString(orderUpdatedEvent));

                orderEventRepository.save(newOrderEvent);

                CompletableFuture<SendResult<String, Object>> future = kafkaProducer.sendOrderUpdated(orderUpdatedEvent);
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

    @SneakyThrows
    @Override
    public void cancelOrder(CancelOrderCommand cancelOrderCommand) {
        OrderCancelledEvent orderToCancel = new OrderCancelledEvent(cancelOrderCommand.id(), OrderStatus.CANCELED.getName());
        OrderEvent orderCanceledEvent = new OrderEvent(orderToCancel.getId().toString(), EventType.ORDER_CANCELED.getName(), objectMapper.writeValueAsString(orderToCancel));
        orderEventRepository.save(orderCanceledEvent);
    }

    @Override
    public Order getOrderById(OrderByIdQuery query) {
        List<OrderEvent> orderEvent = orderProjection.handle(query);

        return orderEventUtility.buildOrder(orderEvent, query);
    }


}

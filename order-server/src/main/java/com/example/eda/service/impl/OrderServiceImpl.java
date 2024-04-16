package com.example.eda.service.impl;

import com.example.eda.cqrs.command.CancelOrderCommand;
import com.example.eda.cqrs.command.CreateOrderCommand;
import com.example.eda.cqrs.command.UpdateOrderCommand;
import com.example.eda.cqrs.projection.OrderProjection;
import com.example.eda.cqrs.query.OrderByIdQuery;
import com.example.eda.cqrs.query.OrdersByStatusQuery;
import com.example.eda.enums.EventType;
import com.example.eda.enums.OrderStatus;
import com.example.eda.event.OrderCancelledEvent;
import com.example.eda.event.OrderCreatedEvent;
import com.example.eda.event.OrderUpdatedEvent;
import com.example.eda.model.Order;
import com.example.eda.model.OrderEvent;
import com.example.eda.producer.KafkaProducer;
import com.example.eda.repository.OrderEventRepository;
import com.example.eda.service.OrderReadService;
import com.example.eda.service.OrderWriteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderWriteService, OrderReadService {
    private final OrderEventRepository orderEventRepository;
    private final OrderProjection orderProjection;
    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;


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
        //TODO here u should recreate Order state (grab all OrderEvents linked with order) -> compare order state with command -> if smth changed save event like orderCancelled Event/Price changed.
        OrderUpdatedEvent orderUpdatedEvent = new OrderUpdatedEvent(updateOrderCommand.id(), updateOrderCommand.status());
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
        //TODO if order was cancelled u will never find it here as cancelled, here u should go build latest stater of order and return it.
        List<OrderEvent> orderEvent = orderProjection.  handle(query);
        Order orderToReturn = new Order();
        AtomicReference<Object> specificEvent = new AtomicReference<>(new Object());
        orderToReturn.setId(query.aggregateId());
        orderEvent.forEach(event -> {
            if (event.getEventType().equals(EventType.ORDER_CREATED.getName())) {
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
            if (event.getEventType().equals(EventType.ORDER_UPDATED.getName())) {
                try {
                    specificEvent.set(objectMapper.readValue(event.getEventData(), OrderUpdatedEvent.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                OrderUpdatedEvent orderUpdatedEvent = (OrderUpdatedEvent) specificEvent.get();
                orderToReturn.setStatus(orderUpdatedEvent.getStatus());
            }
        });

        return orderToReturn;
    }

    @Override
    public List<Order> getOrdersByStatus(OrdersByStatusQuery ordersByStatusQuery) {
        List<OrderEvent> handle = orderProjection.handle(ordersByStatusQuery);
        List<Order> ordersToReturn = new ArrayList<>();
        return ordersToReturn;

    }


}

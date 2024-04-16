package com.example.eda.util;

import com.example.eda.cqrs.query.OrderByIdQuery;
import com.example.eda.enums.EventType;
import com.example.eda.event.OrderCancelledEvent;
import com.example.eda.event.OrderCreatedEvent;
import com.example.eda.event.OrderEvent;
import com.example.eda.event.OrderUpdatedEvent;
import com.example.eda.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class OrderEventUtility {

    private final ObjectMapper objectMapper;

    public Order buildOrder(List<OrderEvent> orderEvent, OrderByIdQuery query) {
        Order orderToReturn = new Order();
        AtomicReference<Object> specificEvent = new AtomicReference<>(new Object());
        orderToReturn.setId(query.aggregateId());
        orderEvent.forEach(event -> {
            if (event.getEventType().equals(EventType.ORDER_CREATED.getName())) {
                OrderCreatedEvent createdOrder = specifyEvent(specificEvent, event, OrderCreatedEvent.class);
                orderToReturn.setOrderDestination(createdOrder.getOrderDestination());
                orderToReturn.setStatus(createdOrder.getStatus());
                orderToReturn.setItem(createdOrder.getItem());
                orderToReturn.setTotalCost(createdOrder.getTotalCost());
                orderToReturn.setUserId(createdOrder.getUserId());
            }
            if (event.getEventType().equals(EventType.ORDER_UPDATED.getName())) {
                OrderUpdatedEvent orderUpdatedEvent = specifyEvent(specificEvent, event, OrderUpdatedEvent.class);
                orderToReturn.setStatus(orderUpdatedEvent.getStatus());
            }
            if (event.getEventType().equals(EventType.ORDER_CANCELED.getName())) {
                OrderCancelledEvent canceledOrder = specifyEvent(specificEvent, event, OrderCancelledEvent.class);
                orderToReturn.setStatus(canceledOrder.getStatus());

            }
        });
        return orderToReturn;
    }

    private <T> T specifyEvent(AtomicReference<Object> specificEvent, OrderEvent event, Class<T> tClass) {
        try {
            specificEvent.set(objectMapper.readValue(event.getEventData(), tClass));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return tClass.cast(specificEvent.get());
    }


}

package com.example.eda.service.impl;

import com.example.eda.event.OrderCreatedEvent;
import com.example.eda.event.OrderUpdatedEvent;
import com.example.eda.model.Order;
import com.example.eda.repository.OrderReadRepository;
import com.example.eda.service.OrderWriteReadSyncService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderWriteReadSyncServiceImpl implements OrderWriteReadSyncService {
   // private final OrderReadRepository orderReadRepository;

    public OrderWriteReadSyncServiceImpl() {
      //  this.orderReadRepository = orderReadRepository;
    }


    @Override
    public void syncOrderCreate(OrderCreatedEvent orderCreatedEvent) {
        var order = new Order(orderCreatedEvent.getId(), orderCreatedEvent.getItem(), orderCreatedEvent.getStatus(),
                orderCreatedEvent.getTotalCost(), orderCreatedEvent.getOrderDestination(), orderCreatedEvent.getUserId());
       // orderReadRepository.save(order);
    }

    @Override
    @Transactional
    public void syncOrderUpdate(OrderUpdatedEvent orderUpdatedEvent) {
      //  orderReadRepository.updateOrderStatusById(orderUpdatedEvent.getId(), orderUpdatedEvent.getStatus());
    }
}

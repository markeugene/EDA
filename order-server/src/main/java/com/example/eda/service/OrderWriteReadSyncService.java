package com.example.eda.service;

import com.example.eda.event.OrderCreatedEvent;
import com.example.eda.event.OrderUpdatedEvent;

public interface OrderWriteReadSyncService {

    void syncOrderCreate(OrderCreatedEvent orderCreatedEvent);

    void syncOrderUpdate(OrderUpdatedEvent orderUpdatedEvent);
}

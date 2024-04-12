package com.example.eda.service;

import com.example.eda.cqrs.command.CreateOrderCommand;
import com.example.eda.cqrs.command.UpdateOrderCommand;

public interface OrderWriteService {
     void createOrder(CreateOrderCommand createOrderCommand);

     void updateOrder(UpdateOrderCommand updateOrderCommand);
}

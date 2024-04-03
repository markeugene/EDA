package com.example.eda.service;

import com.example.eda.CreateOrder;
import com.example.eda.UpdateOrder;

public interface OrderWriteService {
    public void createOrder(CreateOrder createOrder);

    public void updateOrder(UpdateOrder updateOrder);
}

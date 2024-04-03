package com.example.eda.service;

import com.example.eda.model.Order;

public interface OrderReadService {

     Order getOrderById(String orderId);
}

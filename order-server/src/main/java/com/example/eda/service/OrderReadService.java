package com.example.eda.service;

import com.example.eda.cqrs.query.OrderByIdQuery;
import com.example.eda.model.Order;

public interface OrderReadService {
    Order getOrderById(OrderByIdQuery orderByIdQuery);

}

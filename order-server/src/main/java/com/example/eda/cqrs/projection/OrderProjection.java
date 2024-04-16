package com.example.eda.cqrs.projection;

import com.example.eda.cqrs.query.OrderByIdQuery;
import com.example.eda.cqrs.query.OrdersByStatusQuery;
import com.example.eda.event.OrderEvent;
import com.example.eda.repository.OrderEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProjection {

    private final OrderEventRepository repository;



    public List<OrderEvent> handle(OrderByIdQuery orderByIdQuery) {
        return repository.findAllByAggregateIdOrderByAggregateIdDesc(orderByIdQuery.aggregateId().toString());
    }

    public List<OrderEvent> handle(OrdersByStatusQuery ordersByStatusQuery) {
        return repository.findAllByStatus(ordersByStatusQuery.status());
    }

}

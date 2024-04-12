package com.example.eda.cqrs.projection;

import com.example.eda.Order;
import com.example.eda.cqrs.query.FirstOrderByIdQuery;
import com.example.eda.cqrs.query.OrderByStatusQuery;
import com.example.eda.cqrs.query.OrdersByIdQuery;
import com.example.eda.model.OrderEvent;
import com.example.eda.repository.OrderEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderProjection {

    private final OrderEventRepository repository;

    public List<OrderEvent> handle(OrdersByIdQuery ordersByIdQuery) {
        return repository.findAllByAggregateId(ordersByIdQuery.aggregateId().toString());
    }

    public Optional<OrderEvent> handle(FirstOrderByIdQuery firstOrderByIdQuery){
        return repository.findFirstByAggregateIdOrderByAggregateIdDesc(firstOrderByIdQuery.aggregateId().toString());
    }

    public List<OrderEvent> handle(OrderByStatusQuery orderByStatusQuery){
        return repository.findAllByStatus(orderByStatusQuery.status());
    }

}

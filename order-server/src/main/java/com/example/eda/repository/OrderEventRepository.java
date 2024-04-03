package com.example.eda.repository;

import com.example.eda.model.OrderEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderEventRepository extends MongoRepository<OrderEvent, String> {
    Optional<OrderEvent> findFirstByAggregateIdOrderByAggregateIdDesc(String orderId);

    List<OrderEvent> findAllByAggregateId(String orderId);
}

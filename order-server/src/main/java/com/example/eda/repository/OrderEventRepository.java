package com.example.eda.repository;

import com.example.eda.model.OrderEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface OrderEventRepository extends MongoRepository<OrderEvent, String> {
    Optional<OrderEvent> findFirstByAggregateIdOrderByAggregateIdDesc(String orderId);

    List<OrderEvent> findAllByAggregateId(String orderId);
    @Query("{ 'eventData.status' : ?0 }")
    List<OrderEvent> findAllByStatus(String status);
}

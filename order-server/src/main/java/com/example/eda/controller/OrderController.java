package com.example.eda.controller;

import com.example.eda.cqrs.aggregator.OrderAggregator;
import com.example.eda.cqrs.command.CreateOrderCommand;
import com.example.eda.cqrs.command.UpdateOrderCommand;
import com.example.eda.model.Order;
import com.example.eda.service.OrderReadService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderAggregator orderAggregator;
    private final OrderReadService orderReadService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderCommand orderToCreate) throws JsonProcessingException {

        if (orderToCreate.quantity() < 1) {
            return ResponseEntity.badRequest().build();
        }
        orderAggregator.handleCreateOrderCommand(orderToCreate);


        return ResponseEntity.ok("Success");
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> updateOrder(@RequestBody UpdateOrderCommand updateOrderCommand) throws JsonProcessingException {

        orderAggregator.handleUpdateOrderCommand(updateOrderCommand);

        return ResponseEntity.ok("Success");
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable String id) throws JsonProcessingException {

        Order orderById = orderReadService.getOrderById(id);

        return ResponseEntity.ok(orderById);
    }

}

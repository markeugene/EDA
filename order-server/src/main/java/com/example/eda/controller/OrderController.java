package com.example.eda.controller;

import com.example.eda.cqrs.aggregator.OrderAggregator;
import com.example.eda.cqrs.command.CancelOrderCommand;
import com.example.eda.cqrs.command.CreateOrderCommand;
import com.example.eda.cqrs.command.UpdateOrderCommand;
import com.example.eda.cqrs.query.OrderByIdQuery;
import com.example.eda.model.Order;
import com.example.eda.service.OrderReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderAggregator orderAggregator;
    private final OrderReadService orderReadService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderCommand orderToCreate) {

        if (orderToCreate.quantity() < 1) {
            return ResponseEntity.badRequest().build();
        }
        orderAggregator.handleCreateOrderCommand(orderToCreate);


        return ResponseEntity.ok("Success");
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> updateOrder(@RequestBody UpdateOrderCommand updateOrderCommand) {

        orderAggregator.handleUpdateOrderCommand(updateOrderCommand);

        return ResponseEntity.ok("Success");
    }

    @DeleteMapping(path = "/cancel")
    public ResponseEntity<String> cancelOrder(@RequestBody CancelOrderCommand cancelOrderCommand) {

        orderAggregator.handeCancelOrderCommand(cancelOrderCommand);

        return ResponseEntity.ok("Success");
    }


    @GetMapping
    public ResponseEntity<Order> getOrder(@RequestBody OrderByIdQuery orderByIdQuery) {

        Order orderById = orderReadService.getOrderById(orderByIdQuery);

        return ResponseEntity.ok(orderById);
    }


}

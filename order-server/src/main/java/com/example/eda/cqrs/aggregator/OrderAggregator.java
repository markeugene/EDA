package com.example.eda.cqrs.aggregator;

import com.example.eda.cqrs.command.CancelOrderCommand;
import com.example.eda.cqrs.command.CreateOrderCommand;

import com.example.eda.cqrs.command.UpdateOrderCommand;
import com.example.eda.service.OrderWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderAggregator {
    private final OrderWriteService orderService;

    public void handleCreateOrderCommand(CreateOrderCommand createCommand) {
        orderService.createOrder(createCommand);
    }

    public void handleUpdateOrderCommand(UpdateOrderCommand updateCommand) {
        orderService.updateOrder(updateCommand);
    }

    public void handeCancelOrderCommand(CancelOrderCommand cancelOrderCommand){
        //orderService.cancelOrder(cancelOrderCommand);
    }
}

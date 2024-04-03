package com.example.eda.handler;

import com.example.eda.command.OrderCreateCommand;
import com.example.eda.service.OrderWriteService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class OrderCommandHandler {
    private final OrderWriteService orderService;

    public OrderCommandHandler(OrderWriteService orderService) {
        this.orderService = orderService;
    }

    @SneakyThrows
    public void handle(OrderCreateCommand createCommand) {
        //orderService.createOrder(createCommand);
    }
}

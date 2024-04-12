package com.example.eda.cqrs.command;

import lombok.Builder;

@Builder
public record CreateOrderCommand(String item,
                                 Integer quantity,
                                 Double totalCost,
                                 String orderDestination,
                                 String userId) {
}

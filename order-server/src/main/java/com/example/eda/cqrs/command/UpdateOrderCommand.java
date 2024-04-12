package com.example.eda.cqrs.command;

import lombok.Builder;

@Builder
public record UpdateOrderCommand(Long id,
                                 String status,
                                 String item,
                                 Integer quantity,
                                 Double totalCost,
                                 String orderDestination,
                                 String userId){
}

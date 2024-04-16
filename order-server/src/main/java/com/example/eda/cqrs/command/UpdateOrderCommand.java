package com.example.eda.cqrs.command;

import lombok.Builder;

@Builder
public record UpdateOrderCommand(Long id,
                                 String status,
                                 String orderDestination,
                                 String userId){
}

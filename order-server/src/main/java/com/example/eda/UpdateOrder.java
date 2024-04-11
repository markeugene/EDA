package com.example.eda;

import lombok.Builder;

@Builder
public record UpdateOrder (Long id, String status,String item, Integer quantity, Double totalCost, String orderDestination,String userId){
}

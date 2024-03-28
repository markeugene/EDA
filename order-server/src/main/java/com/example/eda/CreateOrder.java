package com.example.eda;

import lombok.Builder;

@Builder
public record CreateOrder(String item, Integer quantity, Double totalCost, String orderDestination) {
}

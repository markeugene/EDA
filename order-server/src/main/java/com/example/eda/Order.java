package com.example.eda;

import lombok.Builder;


@Builder
public record Order(String id, String item, Integer quantity, String status, Double totalCost, String orderDestination,
                    String userId) {
}

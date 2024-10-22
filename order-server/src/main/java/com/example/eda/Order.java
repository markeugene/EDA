package com.example.eda;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Order(UUID id, String item, Integer quantity, Double totalCost, String orderDestination, UUID userId) {
}

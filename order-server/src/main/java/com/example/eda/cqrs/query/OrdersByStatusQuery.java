package com.example.eda.cqrs.query;

import lombok.Builder;

@Builder
public record OrdersByStatusQuery(String status) {
}

package com.example.eda.cqrs.query;

import lombok.Builder;

@Builder
public record FirstOrderByIdQuery(Long aggregateId) {
}

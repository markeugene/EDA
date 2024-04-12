package com.example.eda.cqrs.command;


import lombok.Builder;

@Builder
public record CancelOrderCommand(Long id) {
}

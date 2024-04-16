package com.example.eda.enums;

import lombok.Getter;

@Getter
public enum EventType {
    ORDER_CREATED("ORDER_CREATED"), ORDER_UPDATED("ORDER_UPDATED"), ORDER_CANCELED("ORDER_CANCELED");

    private final String name;

    EventType(String type) {
        this.name = type;
    }
}

package com.example.eda.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("CREATED"), UPDATED("UPDATED"), CANCELED("CANCELED");

    private final String name;

    OrderStatus(String status) {
        this.name = status;
    }
}

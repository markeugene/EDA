package com.example.eda.command;

import lombok.Data;

@Data
public class OrderCreateCommand{
    private String userId;
    private String destination;
    private Double totalCost;
    private String item;
}

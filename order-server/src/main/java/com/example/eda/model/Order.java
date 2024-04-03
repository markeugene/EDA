package com.example.eda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

    private Long id;
    private String item;
    private String status;
    private Double totalCost;
    private String orderDestination;
    private String userId;
}

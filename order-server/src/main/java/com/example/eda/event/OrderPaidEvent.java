package com.example.eda.event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderPaidEvent{
    private Long id;
    private String item;
    private String status;
    private Double totalCost;
    private String orderDestination;
    private String userId;
}

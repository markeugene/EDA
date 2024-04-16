package com.example.eda.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderUpdatedEvent {
    private Long id;
    private String status;
    private String orderDestination;
}

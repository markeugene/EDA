package com.example.eda.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderUpdatedEvent {
    //TODO add price here, or quantity whatever
    private Long id;
    private String status;
}

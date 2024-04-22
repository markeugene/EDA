package com.example.eda.event;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "order_events")
@NoArgsConstructor
@Data
public class OrderEvent {
    @MongoId
    private String id;
    private String aggregateId;
    private String eventType;
    private String eventData;

    public OrderEvent(String aggregateId, String eventType, String eventData) {
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.eventData = eventData;
    }
}

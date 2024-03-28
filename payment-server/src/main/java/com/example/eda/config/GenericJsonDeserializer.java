package com.example.eda.config;

import com.example.eda.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GenericJsonDeserializer implements Deserializer<Order> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Class<Object> targetType = Object.class;


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No additional configuration needed
    }

    @Override
    public Order deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Order.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing JSON message", e);
        }
    }

    @Override
    public void close() {
        // No resources to close
    }
}

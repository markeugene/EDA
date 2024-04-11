package com.example.eda.mapper;

import com.example.eda.Order;
import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.Mapping;


@Mapper(componentModel = "SPRING")
public interface OrderMapper {

    Order mapTo(com.example.eda.model.Order order);

    com.example.eda.model.Order mapTo(Order orderDto);
}

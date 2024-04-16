package com.example.eda.service.impl;

import com.example.eda.repository.OrderEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderUtility {

    private final OrderEventRepository orderEventRepository;


}

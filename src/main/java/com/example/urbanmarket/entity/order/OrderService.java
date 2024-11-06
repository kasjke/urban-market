package com.example.urbanmarket.entity.order;

import com.example.urbanmarket.dto.request.OrderRequestDto;
import com.example.urbanmarket.dto.response.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto create(OrderRequestDto orderRequestDto);

   OrderResponseDto getById(String id);

    List<OrderResponseDto> getAllByUserId(String userId);

    OrderResponseDto update(String id, OrderRequestDto orderRequestDto);

    void delete(String id);
}

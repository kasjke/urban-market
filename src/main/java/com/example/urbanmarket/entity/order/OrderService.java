package com.example.urbanmarket.entity.order;

import com.example.urbanmarket.dto.request.OrderRequestDto;
import com.example.urbanmarket.dto.response.OrderResponseDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    Optional<OrderResponseDto> getOrderById(String id);

    List<OrderResponseDto> getOrdersByUserId(String userId);

    OrderResponseDto updateOrder(String id, OrderRequestDto orderRequestDto);

    void deleteOrder(String id);
}

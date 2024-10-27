package com.example.urbanmarket.entity.order;

import com.example.urbanmarket.dto.request.OrderRequestDto;
import com.example.urbanmarket.dto.response.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

   OrderResponseDto getOrderById(String id);

    List<OrderResponseDto> getOrdersByUserId(String userId);

    OrderResponseDto updateOrder(String id, OrderRequestDto orderRequestDto);

    void deleteOrder(String id);
}

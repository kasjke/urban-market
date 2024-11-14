package com.example.urbanmarket.dto.response;

import com.example.urbanmarket.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        String id,
        String userId,
        List<String> productIds,
        int totalPrice,
        OrderStatus status,
        String shippingAddress,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

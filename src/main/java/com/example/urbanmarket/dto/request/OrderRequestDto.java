package com.example.urbanmarket.dto.request;

import com.example.urbanmarket.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequestDto(
        @NotNull String userId,
        @NotNull List<String> productIds,
        @NotNull int totalPrice,
        @NotNull OrderStatus status,
        String shippingAddress
) {}

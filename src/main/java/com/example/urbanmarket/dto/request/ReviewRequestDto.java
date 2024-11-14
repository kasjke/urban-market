package com.example.urbanmarket.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRequestDto(
        @NotNull(message = "Product ID cannot be null") String productId,
        @NotNull(message = "User ID cannot be null") String userId,
        @NotNull(message = "Content cannot be null")
        @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters") String content,
        @NotNull(message = "Rating cannot be null") double rating
) {}

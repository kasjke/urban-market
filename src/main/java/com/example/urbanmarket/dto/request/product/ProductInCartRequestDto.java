package com.example.urbanmarket.dto.request.product;

import jakarta.validation.constraints.NotBlank;

public record ProductInCartRequestDto(@NotBlank String id, int amount) {
}

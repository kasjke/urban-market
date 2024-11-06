package com.example.urbanmarket.dto.response.product;

public record ProductInCartOrderResponseDto(String productId, String name, String image, int amount, int price) {
}

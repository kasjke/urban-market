package com.example.urbanmarket.dto.response;

import com.example.urbanmarket.dto.response.product.ProductInCartOrderResponseDto;

import java.util.List;

public record CartResponseDto(String id, List<ProductInCartOrderResponseDto> figures, int totalPrice) {
}


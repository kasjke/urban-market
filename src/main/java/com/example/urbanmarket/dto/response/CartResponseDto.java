package com.example.urbanmarket.dto.response;

import java.util.List;

public record CartResponseDto(String id, List<ProductInCartOrderResponseDto> figures, int totalPrice) {
}


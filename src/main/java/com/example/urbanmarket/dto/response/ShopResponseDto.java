package com.example.urbanmarket.dto.response;

import com.example.urbanmarket.dto.response.product.ProductResponseDto;

import java.util.List;

public record ShopResponseDto(String id, String name, String description,
                              String logo, List<ProductResponseDto> products) {
}

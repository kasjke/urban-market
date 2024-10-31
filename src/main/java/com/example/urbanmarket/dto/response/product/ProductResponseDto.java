package com.example.urbanmarket.dto.response.product;

import com.example.urbanmarket.entity.product.sections.SubCategory;

import java.util.List;

public record ProductResponseDto(String id, String name, String description, SubCategory subCategory,
                                 int currentPrice, int oldPrice, int amount,
                                 List<String> images, int purchaseCount, String shopId) {
}

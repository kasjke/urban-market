package com.example.urbanmarket.dto.request;

import com.example.urbanmarket.entity.product.sections.SubCategory;

import java.util.List;

public record ProductRequestDto(String name, String description, SubCategory subCategory,
                                int currentPrice, int amount,
                                List<String> images, String shopId) {
}
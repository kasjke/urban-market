package com.example.urbanmarket.dto.request;

import com.example.urbanmarket.entity.product.sections.SubCategory;
import com.example.urbanmarket.enums.Color;
import com.example.urbanmarket.enums.ProductSize;

import java.util.List;

public record ProductRequestDto(String name,
                                String description,
                                SubCategory subCategory,
                                int currentPrice,
                                int amount,
                                String brandCollection,
                                String DeliverReturn,
                                List<Color> color,
                                List<ProductSize> product_sizes,
                                List<String> images,
                                String shopId) {
}
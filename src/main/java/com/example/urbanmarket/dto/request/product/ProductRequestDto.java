package com.example.urbanmarket.dto.request.product;

import com.example.urbanmarket.entity.product.sections.SubCategory;
import com.example.urbanmarket.enums.Color;
import com.example.urbanmarket.enums.ProductSize;

import java.util.List;

public record ProductRequestDto(String name,
                                String description,
                                String features,
                                String brandCollection,
                                SubCategory subCategory,
                                int currentPrice,
                                int oldPrice,
                                List<String> images,
                                String DeliverReturn,
                                List<ProductSize> product_sizes,
                                List<Color> color,
                                String shopId) {
}

package com.example.urbanmarket.dto.request;

import com.example.urbanmarket.entity.product.sections.SubCategory;
import com.example.urbanmarket.enums.Color;
import com.example.urbanmarket.enums.ProductSize;

import java.util.List;

public record ProductAddDto(String name,
                            String description,
                            String features,
                            String brandCollection,
                            SubCategory subCategory,
                            int amount,
                            int currentPrice,
                            String DeliverReturn,
                            List<ProductSize> product_sizes,
                            List<Color> color,
                            String shopId) {
}

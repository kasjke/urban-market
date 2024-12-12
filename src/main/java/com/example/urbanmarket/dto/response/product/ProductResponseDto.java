package com.example.urbanmarket.dto.response.product;

import com.example.urbanmarket.dto.response.ReviewResponseDto;
import com.example.urbanmarket.entity.product.sections.SubCategory;
import com.example.urbanmarket.enums.Color;
import com.example.urbanmarket.enums.ProductSize;

import java.util.List;

public record ProductResponseDto(
        String id,
        String name,
        String description,
        SubCategory subCategory,
        int currentPrice,
        int oldPrice,
        int amount,
        String brandCollection,
        String DeliverReturn,
        List<Color> color,
        List<ProductSize> product_sizes,
        List<String> images,
        int purchaseCount,
        String shopId,
        double averageRating,
        List<ReviewResponseDto> reviews
) {}

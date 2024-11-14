package com.example.urbanmarket.dto.response.product;

import com.example.urbanmarket.entity.product.sections.SubCategory;

import java.util.List;

public record ProductResponseYouMayAlsoDto(
        String id,
        String name,
        SubCategory subCategory,
        int currentPrice,
        int oldPrice,
        String image,
        String shopName
  ){
}

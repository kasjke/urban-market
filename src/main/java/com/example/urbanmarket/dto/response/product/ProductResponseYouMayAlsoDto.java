package com.example.urbanmarket.dto.response.product;

import com.example.urbanmarket.entity.product.sections.SubCategory;

import java.util.List;

public record ProductResponseYouMayAlsoDto(
    String name,
    String description,
    SubCategory subCategory,
    int currentPrice,
    int oldPrice,
    List<String> images,
    int purchaseCount,
    String shopName
  ){
}

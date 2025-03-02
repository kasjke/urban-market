package com.example.urbanmarket.dto.response.product;

import com.example.urbanmarket.enums.Color;
import com.example.urbanmarket.enums.ProductSize;

import java.util.List;


public record ProductResponseYouMayAlsoDto(
        String id,
        String name,
        String subCategory,
        int currentPrice,
        int oldPrice,
        String brandCollection,
        String DeliverReturn,
        List<Color> color,
        List<ProductSize> product_sizes,
        String shopName
  ){
}

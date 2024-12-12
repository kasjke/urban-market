package com.example.urbanmarket.dto.response.product;

import com.example.urbanmarket.entity.product.sections.SubCategory;
import com.example.urbanmarket.enums.Color;
import com.example.urbanmarket.enums.ProductSize;

import java.util.List;


public record ProductResponseYouMayAlsoDto(
        String id,
        String name,
        SubCategory subCategory,
        int currentPrice,
        int oldPrice,
        String brandCollection,
        String DeliverReturn,
        List<Color> color,
        List<ProductSize> product_sizes,
        String image,
        String shopName
  ){
}

package com.example.urbanmarket.dto.response;

import com.example.urbanmarket.dto.response.product.ProductResponseDto;
import com.example.urbanmarket.entity.shop.contacts.ContactInfo;

import java.util.List;

public record ShopResponseDto(String id, String name, String description,
                              String logo, ContactInfo contacts, List<ProductResponseDto> products) {
}

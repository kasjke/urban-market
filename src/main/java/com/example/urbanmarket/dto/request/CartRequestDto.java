package com.example.urbanmarket.dto.request;

import com.example.urbanmarket.dto.request.product.ProductInCartRequestDto;

import java.util.List;

public record CartRequestDto(String promoCode, List<ProductInCartRequestDto> products) {
}
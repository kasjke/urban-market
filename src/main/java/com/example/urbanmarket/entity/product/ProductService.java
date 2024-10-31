package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.product.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto create(ProductRequestDto requestDto);
    ProductResponseDto getById(String id);
    List<ProductResponseDto> getAll();
    ProductResponseDto update(String id, ProductRequestDto requestDto);
    void delete(String id);
}

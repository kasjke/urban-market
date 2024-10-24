package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dto.request.ProductRequestDto;
import com.example.urbanmarket.dto.response.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto create(ProductRequestDto productDto);
    ProductResponseDto getById(String id);
    List<ProductResponseDto> getAll();
    ProductResponseDto update(String id, ProductRequestDto productDto);
    void delete(String id);
}

package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.product.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductResponseDto> getNewArrivals(Pageable pageable);

    Page<ProductResponseDto> getBestSellers(Pageable pageable);
    ProductResponseDto create(ProductRequestDto requestDto);
    ProductResponseDto getById(String id);
    List<ProductResponseDto> getAll();
    Page<ProductResponseDto> findByOldPriceGreaterThanCurrentPrice(Pageable pageable);
    ProductResponseDto findProductWithReviewsById(String productId);
    ProductResponseDto update(String id, ProductRequestDto requestDto);
    void delete(String id);
}

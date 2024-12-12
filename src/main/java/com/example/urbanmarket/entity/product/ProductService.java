package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dto.request.RequestUpdatePriceDto;
import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.ResponseUpdatePriceDto;
import com.example.urbanmarket.dto.response.product.ProductResponseDto;
import com.example.urbanmarket.dto.response.product.ProductResponseYouMayAlsoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductResponseDto create(ProductRequestDto requestDto);

    ProductResponseDto getById(String id);

    List<ProductResponseDto> getAll();
    Page<ProductResponseDto> getNewArrivals(Pageable pageable);
    Page<ProductResponseDto> getBestSellers(Pageable pageable);
    Page<ProductResponseDto> findByOldPriceGreaterThanCurrentPrice(Pageable pageable);

    ProductResponseDto update(String id, ProductRequestDto requestDto);

    void delete(String id);

    List<ProductResponseYouMayAlsoDto> findSimilarProducts(String id);

    ResponseUpdatePriceDto updateProductPrice(String productId, RequestUpdatePriceDto requestUpdatePriceDto);
}

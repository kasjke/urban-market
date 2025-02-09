package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dto.request.RequestUpdatePriceDto;
import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.ResponseUpdatePriceDto;
import com.example.urbanmarket.dto.response.product.ProductResponseDto;
import com.example.urbanmarket.dto.response.product.ProductResponseYouMayAlsoDto;
import com.example.urbanmarket.enums.Color;
import com.example.urbanmarket.enums.ProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponseDto create(ProductRequestDto requestDto);

    ProductResponseDto getById(String id);

    List<ProductResponseDto> getAll();
    List<ProductResponseDto> getNewArrivals();
    List<ProductResponseDto> getBestSellers();
    List<ProductResponseDto> findByOldPriceGreaterThanCurrentPrice();

    ProductResponseDto update(String id, ProductRequestDto requestDto);

    void delete(String id);
     Page<ProductResponseDto> getFilteredProducts(

            String categoryName,
            String createdAt,
            String price,
            Integer priceMin,
            Integer priceMax,
            Color color,
            ProductSize size,
            Pageable pageable
    );
    List<ProductResponseYouMayAlsoDto> findSimilarProducts(String id);

    ResponseUpdatePriceDto updateProductPrice(String productId, RequestUpdatePriceDto requestUpdatePriceDto);

    //String addProduct(ProductAddDto productAddDto, MultipartFile titleImageFile, List<MultipartFile> additionalImageFiles);
    String addProduct(String productId, MultipartFile titleImageFile, List<MultipartFile> additionalImageFiles);

    Page<ProductResponseDto> getProductsSortedByPrice(String sortDirection, Pageable pageable);
    List<ProductResponseDto> getProductsByCategories(String categories);
}

package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.ProductResponseDto;
import com.example.urbanmarket.entity.product.sections.SubCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductResponseDto toResponseDto(ProductEntity entity);

    ProductEntity toEntity(ProductResponseDto dto);
    ProductEntity toEntity(ProductRequestDto dto);

    List<ProductResponseDto> toResponseDtoList (List<ProductEntity> entities);
    List<ProductEntity> toEntityList(List<ProductResponseDto> dtos);

    default SubCategory map(String subCategory) {
        if (subCategory == null) {
            return null;
        }
        return SubCategory.valueOf(subCategory);
    }
}

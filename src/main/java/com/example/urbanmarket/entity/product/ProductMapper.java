package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.config.CustomMapperConfig;
import com.example.urbanmarket.dto.request.ProductRequestDto;
import com.example.urbanmarket.dto.response.ProductResponseDto;
import com.example.urbanmarket.entity.product.sections.SubCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", config = CustomMapperConfig.class)
public interface ProductMapper {
     ProductResponseDto toResponseDto(ProductEntity entity);

     ProductEntity toEntity(ProductResponseDto dto);


     ProductEntity toEntity(ProductRequestDto dto);

    List<ProductResponseDto> toListEntity (List<ProductEntity> productEntities);
    default SubCategory map(String subCategory) {
        if (subCategory == null) {
            return null;
        }
        return SubCategory.valueOf(subCategory);
    }
}

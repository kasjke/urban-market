package com.example.urbanmarket.entity.product;

import com.example.urbanmarket.config.CustomMapperConfig;
import com.example.urbanmarket.dto.request.product.ProductRequestDto;
import com.example.urbanmarket.dto.response.product.ProductResponseDto;
import com.example.urbanmarket.entity.product.sections.SubCategory;
import com.example.urbanmarket.entity.user.review.ReviewMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", config = CustomMapperConfig.class, uses = ReviewMapper.class)
public interface ProductMapper {

    ProductEntity toEntity(ProductResponseDto dto);

    ProductEntity toEntity(ProductRequestDto dto);

    ProductResponseDto toResponseDto(ProductEntity entity);

//    @Mapping(target = "oldPrice", ignore = true)
//    ProductResponseYouMayAlsoDto toYouMayAlsoDto(ProductEntity entity);

    List<ProductResponseDto> toResponseDtoList(List<ProductEntity> entities);

    List<ProductEntity> toEntityList(List<ProductResponseDto> dtos);

    default SubCategory map(String subCategory) {
        if (subCategory == null) {
            return null;
        }
        return SubCategory.valueOf(subCategory);
    }
}
